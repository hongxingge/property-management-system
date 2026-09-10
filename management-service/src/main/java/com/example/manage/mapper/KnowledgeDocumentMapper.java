package com.example.manage.mapper;

import com.example.manage.bean.KnowledgeDocumentBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName：KnowledgeDocumentMapper
 * @Author: uu205
 * @Date: 2026/9/10 19:46
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Mapper
public interface KnowledgeDocumentMapper {
    int insertDocument(KnowledgeDocumentBean document);
    int deleteDocument(@Param("id") Integer id);
    List<KnowledgeDocumentBean> listDocuments();
}
