package com.example.manage.service;

import com.example.manage.bean.KnowledgeChunkBean;
import com.example.manage.bean.KnowledgeDocumentBean;
import com.example.manage.mapper.KnowledgeChunkMapper;
import com.example.manage.mapper.KnowledgeDocumentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2026/9/10 19:51
 */
@Service
public class KnowledgeService {

    @Resource
    private KnowledgeDocumentMapper documentMapper;

    @Resource
    private KnowledgeChunkMapper chunkMapper;

    @Resource
    private EmbeddingService embeddingService;

    /** 上传知识:存文档 -> 切分 -> embedding -> 存片段 */
    @Transactional   // 事务:文档和片段要么都存成功,要么都回滚,避免数据不一致
    public void addKnowledge(String title, String content) {
        // 1. 存文档
        KnowledgeDocumentBean doc = new KnowledgeDocumentBean();
        doc.setTitle(title);
        doc.setContent(content);
        doc.setStatus(1);
        doc.setCreatedAt(System.currentTimeMillis());
        documentMapper.insertDocument(doc);   // useGeneratedKeys 回填 doc.id

        // 2. 按空行切分
        List<String> chunks = splitChunks(content);

        // 3. 批量 embedding
        List<float[]> vectors = embeddingService.embed(chunks);

        // 4. 存片段(内容 + 向量)
        for (int i = 0; i < chunks.size(); i++) {
            KnowledgeChunkBean chunk = new KnowledgeChunkBean();
            chunk.setDocumentId(doc.getId());
            chunk.setContent(chunks.get(i));
            chunk.setEmbedding(embeddingService.vectorToJson(vectors.get(i)));
            chunk.setChunkIndex(i);
            chunk.setCreatedAt(System.currentTimeMillis());
            chunkMapper.insertChunk(chunk);
        }
    }

    /** 删除知识:删文档 + 删它的所有片段 */
    @Transactional
    public void deleteKnowledge(Integer id) {
        documentMapper.deleteDocument(id);
        chunkMapper.deleteByDocumentId(id);
    }

    /** 查文档列表 */
    public List<KnowledgeDocumentBean> listDocuments() {
        return documentMapper.listDocuments();
    }

    /** 按空行切分内容 */
    private List<String> splitChunks(String content) {
        String[] parts = content.split("\\n\\s*\\n");
        List<String> chunks = new ArrayList<>();
        for (String part : parts) {
            String chunk = part.trim();
            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }
        }
        return chunks;
    }
}