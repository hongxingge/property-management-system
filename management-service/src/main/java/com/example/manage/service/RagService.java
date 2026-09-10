package com.example.manage.service;

import com.example.manage.bean.KnowledgeChunkBean;
import com.example.manage.exception.BizException;
import com.example.manage.mapper.KnowledgeChunkMapper;
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
 *  * RAG 核心服务。
 *  * V2.0 改造:知识源从「写死的 txt + 内存向量」改成「数据库知识库(knowledge_chunk)」。
 *  * 检索时从数据库加载所有片段,解析向量,算余弦相似度取 top-k。
 * @date 2026/9/4 19:25
 */
@Service
public class RagService {

    @Value("${ai.deepseek.api-key}") private String dsKey;
    @Value("${ai.deepseek.base-url}") private String dsBaseUrl;
    @Value("${ai.deepseek.model}") private String dsModel;
    @Value("${ai.top-k}") private int topK;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private EmbeddingService embeddingService;

    @Resource
    private KnowledgeChunkMapper knowledgeChunkMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    /** 余弦相似度:两个向量越接近,值越接近 1 */
    private double cosine(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    /** 检索:从数据库加载所有片段,解析向量算余弦,取 top-k */
    private List<String> retrieve(float[] qVec, int k) {
        List<KnowledgeChunkBean> chunks = knowledgeChunkMapper.listAllChunks();
        List<double[]> scored = new ArrayList<>();   // [相似度, 下标]
        for (int i = 0; i < chunks.size(); i++) {
            float[] vec = embeddingService.jsonToVector(chunks.get(i).getEmbedding());
            scored.add(new double[]{cosine(qVec, vec), i});
        }
        scored.sort((a, b) -> Double.compare(b[0], a[0]));
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, scored.size()); i++) {
            result.add(chunks.get((int) scored.get(i)[1]).getContent());
        }
        return result;
    }

    /** 主入口:问题 -> 向量化 -> 检索 -> 生成 */
    public String ask(String question) {
        float[] qVec = embeddingService.embed(List.of(question)).get(0);
        List<String> contexts = retrieve(qVec, topK);
        String system = "你是小区物业智能客服,只能根据下面提供的物业规定回答;规定里没有的,如实回答不知道,不要编造。";
        String userPrompt = "物业规定如下:\n" + String.join("\n\n", contexts) + "\n\n业主问题:" + question;
        return chat(system, userPrompt);
    }

    /** 调用 DeepSeek 生成回答 */
    private String chat(String system, String userPrompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(dsKey);
            Map<String, Object> body = new HashMap<>();
            body.put("model", dsModel);
            body.put("temperature", 0.3);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", system));
            messages.add(Map.of("role", "user", "content", userPrompt));
            body.put("messages", messages);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            String respJson = restTemplate.postForObject(dsBaseUrl + "/chat/completions", entity, String.class);
            return objectMapper.readTree(respJson)
                    .get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new BizException("智能客服暂时不可用,请稍后再试", e);
        }
    }
}
