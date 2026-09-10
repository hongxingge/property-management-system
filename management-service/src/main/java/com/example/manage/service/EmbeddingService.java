package com.example.manage.service;

import com.example.manage.exception.BizException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description:
 * Embedding 服务:把文本转成向量(调用硅基流动 bge-m3)。
 * 从 RagService 抽出来独立成服务,让「知识上传」和「RAG 检索」都能复用。
 * @date 2026/9/10 19:49
 */
@Service
public class EmbeddingService {

    @Value("${ai.siliconflow.api-key}") private String sfKey;
    @Value("${ai.siliconflow.base-url}") private String sfBaseUrl;
    @Value("${ai.siliconflow.embedding-model}") private String embeddingModel;

    @Resource
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    /** 批量把文本转成向量 */
    public List<float[]> embed(List<String> texts) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sfKey);
            Map<String, Object> body = new HashMap<>();
            body.put("model", embeddingModel);
            body.put("input", texts);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            String respJson = restTemplate.postForObject(sfBaseUrl + "/embeddings", entity, String.class);
            JsonNode data = objectMapper.readTree(respJson).get("data");
            List<float[]> vectors = new ArrayList<>();
            for (JsonNode item : data) {
                JsonNode emb = item.get("embedding");
                float[] vec = new float[emb.size()];
                for (int i = 0; i < emb.size(); i++) {
                    vec[i] = (float) emb.get(i).asDouble();
                }
                vectors.add(vec);
            }
            return vectors;
        } catch (Exception e) {
            throw new BizException("知识库服务不可用,请稍后再试", e);
        }
    }

    /** 向量转 JSON 字符串(存数据库用) */
    public String vectorToJson(float[] vec) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vec.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(vec[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /** JSON 字符串转回向量(检索时用)。向量数据坏了抛业务异常 */
    public float[] jsonToVector(String json) {
        try {
            JsonNode arr = objectMapper.readTree(json);
            float[] vec = new float[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                vec[i] = (float) arr.get(i).asDouble();
            }
            return vec;
        } catch (Exception e) {
            throw new BizException("知识向量数据异常", e);
        }
    }
}