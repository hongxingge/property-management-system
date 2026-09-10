package com.example.manage.bean;

/**
 * @author Administrator
 * @version 1.0
 * @description: 知识库文档：一篇管理员上传的知识文档。
 * @date 2026/9/10 19:45
 */
public class KnowledgeDocumentBean {
    private Integer id;
    private String title;      // 文档标题
    private String content;    // 文档全文
    private Integer status;    // 1启用 0停用
    private Long createdAt;
    private Long updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}