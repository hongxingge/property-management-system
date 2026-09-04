package com.example.manage.controller;

import com.example.manage.bean.ResultBean;
import com.example.manage.service.RagService;
import com.example.manage.utils.ResultUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2026/9/4 19:32
 */
@Controller
@RequestMapping("/chat")
public class ChatController {
    @Resource
    RagService ragService;

    @RequestMapping("/ask")
    @ResponseBody
    public ResultBean ask(@RequestBody Map<String, String> params) {
        String question = params.get("question");
        if (question == null || question.trim().isEmpty()) {
            return ResultUtil.getFailBean("问题不能为空");
        }
        try {
            String answer = ragService.ask(question);
            return ResultUtil.getSuccessBean(answer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getFailBean("智能客服暂时不可用,请稍后再试");
        }
    }
}