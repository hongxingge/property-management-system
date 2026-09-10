package com.example.manage.controller;

import com.example.manage.bean.KnowledgeDocumentBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.config.RequireRole;
import com.example.manage.config.RoleConstant;
import com.example.manage.service.KnowledgeService;
import com.example.manage.utils.ResultUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2026/9/10 20:18
 */
@Controller
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Resource
    private KnowledgeService knowledgeService;

    /** 上传知识(管理员) */
    @RequestMapping("/add")
    @RequireRole(RoleConstant.ADMIN)
    @ResponseBody
    public ResultBean add(@RequestBody Map<String, String> params) {
        String title = params.get("title");
        String content = params.get("content");
        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            return ResultUtil.getFailBean("标题和内容不能为空");
        }
        knowledgeService.addKnowledge(title, content);
        return ResultUtil.getSuccessBean("知识上传成功");
    }

    /** 查文档列表(管理员) */
    @RequestMapping("/list")
    @RequireRole(RoleConstant.ADMIN)
    @ResponseBody
    public ResultBean list() {
        List<KnowledgeDocumentBean> docs = knowledgeService.listDocuments();
        return ResultUtil.getSuccessBean(docs);
    }

    /** 删除知识(管理员) */
    @RequestMapping("/delete")
    @RequireRole(RoleConstant.ADMIN)
    @ResponseBody
    public ResultBean delete(@RequestBody Map<String, Integer> params) {
        knowledgeService.deleteKnowledge(params.get("id"));
        return ResultUtil.getSuccessBean("删除成功");
    }
}