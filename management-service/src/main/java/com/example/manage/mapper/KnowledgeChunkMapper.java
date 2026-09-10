package com.example.manage.mapper;

import com.example.manage.bean.KnowledgeChunkBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName：KnowledgeChunkMapper
 * @Author: uu205
 * @Date: 2026/9/10 19:46
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Mapper
public interface KnowledgeChunkMapper {
    int insertChunk(KnowledgeChunkBean chunk);
    int deleteByDocumentId(@Param("documentId") Integer documentId);
    List<KnowledgeChunkBean> listAllChunks();   // 检索时加载所有片段
}