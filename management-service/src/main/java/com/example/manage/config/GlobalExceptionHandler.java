package com.example.manage.config;

import com.example.manage.bean.ResultBean;
import com.example.manage.utils.ResultUtil;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * @author Administrator
 * @version 1.0
 * @description: 全局异常处理器：统一捕获异常，返回规范的 JSON，避免堆栈和内部信息泄露。
 * @date 2026/9/2 20:33
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 参数校验失败（@Valid 触发）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBean handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResultUtil.getFailBean(msg);
    }

    // 其他所有异常兜底
    @ExceptionHandler(Exception.class)
    public ResultBean handleException(Exception e) {
        return ResultUtil.getFailBean("系统异常，请稍后重试");
    }
}