package com.example.manage.service;

import com.example.manage.exception.BizException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description:  RAG 核心服务。
 *                流程:知识文档 -> 切分 -> embedding 向量化 -> 存内存 -> 提问时检索 top-k -> 喂给 DeepSeek 生成。
 * @date 2026/9/4 19:25
 */
@Service
public class RagService {
    @Value("${ai.siliconflow.api-key}") private String sfKey;
    @Value("${ai.siliconflow.base-url}") private String sfBaseUrl;
    @Value("${ai.siliconflow.embedding-model}") private String embeddingModel;
    @Value("${ai.deepseek.api-key}") private String dsKey;
    @Value("${ai.deepseek.base-url}") private String dsBaseUrl;
    @Value("${ai.deepseek.model}") private String dsModel;
    @Value("${ai.top-k}") private int topK;

    @Resource
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    // 知识库:每条知识文本 + 它的向量
    private final List<String> knowledgeTexts = new ArrayList<>();
    private final List<float[]> knowledgeVectors = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(RagService.class);

    // 启动时加载知识文档并向量化
    @PostConstruct
    public void init() throws Exception {
        ClassPathResource resource = new ClassPathResource("knowledge/property-knowledge.txt");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String[] chunks = content.split("\\n\\s*\\n");   // 按空行切分
        for (String chunk : chunks) {
            chunk = chunk.trim();
            if (!chunk.isEmpty()) {
                knowledgeTexts.add(chunk);
            }
        }
        knowledgeVectors.addAll(embed(knowledgeTexts));
        log.info("【RAG】知识库加载完成,共 {} 条", knowledgeTexts.size());
    }

    /**
     * 调用硅基流动 embedding,把文本转成向量。
     * 调外部 API 失败时抛 BizException——前端看到友好提示,底层堆栈通过 cause 保留。
     */
    private List<float[]> embed(List<String> texts) {
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

    /** 检索与问题最相关的 top-k 条知识 */
    private List<String> retrieve(float[] qVec, int k) {
        List<double[]> scored = new ArrayList<>();   // [相似度, 下标]
        for (int i = 0; i < knowledgeVectors.size(); i++) {
            scored.add(new double[]{cosine(qVec, knowledgeVectors.get(i)), i});
        }
        scored.sort((a, b) -> Double.compare(b[0], a[0]));
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, scored.size()); i++) {
            result.add(knowledgeTexts.get((int) scored.get(i)[1]));
        }
        return result;
    }

    /** 主入口:问题 -> 向量化 -> 检索 -> 生成 */
    public String ask(String question) {
        float[] qVec = embed(List.of(question)).get(0);
        List<String> contexts = retrieve(qVec, topK);
        String system = "你是小区物业智能客服,只能根据下面提供的物业规定回答;规定里没有的,如实回答不知道,不要编造。";
        String userPrompt = "物业规定如下:\n" + String.join("\n\n", contexts) + "\n\n业主问题:" + question;
        return chat(system, userPrompt);
    }

    /** 调用 DeepSeek 生成回答。调外部 API 失败时抛 BizException。 */
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