package com.example.manage.bean;

/**
 * @author Administrator
 * @version 1.0
 * @description: 知识库片段：文档切出来的一个片段 + 它的向量(JSON字符串)。
 * @date 2026/9/10 19:45
 */
public class KnowledgeChunkBean {
    private Integer id;
    private Integer documentId;   // 关联文档
    private String content;       // 片段文本
    private String embedding;     // 向量,JSON数组字符串,如 "[0.1,0.2,...]"
    private Integer chunkIndex;   // 片段序号
    private Long createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getDocumentId() { return documentId; }
    public void setDocumentId(Integer documentId) { this.documentId = documentId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }
    public Integer getChunkIndex() { return chunkIndex; }
    public void setChunkIndex(Integer chunkIndex) { this.chunkIndex = chunkIndex; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}