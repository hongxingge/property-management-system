package com.example.manage.config;

import com.example.manage.bean.ResultBean;
import com.example.manage.exception.BizException;
import com.example.manage.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * @author Administrator
 * @version 1.0
 * @description: 全局异常处理器：统一捕获异常，返回规范的 JSON，避免堆栈和内部信息泄露。
 * @date 2026/9/2 20:33
 */

/**
 * 全局异常处理器：统一捕获异常，返回规范的 JSON，避免堆栈和内部信息泄露。
 *
 * 处理顺序说明：
 * Spring 会找「最匹配」的 @ExceptionHandler 处理异常——
 * BizException 命中 handleBizException，
 * 参数校验失败命中 handleValidException，
 * 剩下所有异常（空指针、SQL 异常、未知错误）都落到 handleException 兜底。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // SLF4J 日志.注意:日志类传本类的 class,日志里才能看到是哪个类打的
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常:message 是开发时明确写好的(比如"账单不存在"),属于「预期的错误」，
     * 不算系统故障,所以只记一条 warn 日志,不打印堆栈.
     */
    @ExceptionHandler(BizException.class)
    public ResultBean handleBizException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResultUtil.getFailBean(e.getMessage());
    }

    /**
     * 参数校验失败(@Valid 触发).取第一条校验失败的提示返回给前端.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBean handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResultUtil.getFailBean(msg);
    }

    /**
     * 兜底:所有没被上面专门处理的异常（空指针、SQL 异常、未知错误）。
     * 关键点:必须用 log.error 把异常堆栈打出来，否则出了问题你根本不知道是哪抛的。
     * 但返回给前端的只能是笼统的"系统异常"，绝不能把堆栈或内部信息暴露出去。
     */
    @ExceptionHandler(Exception.class)
    public ResultBean handleException(Exception e) {
        log.error("系统异常", e);   // 第二个参数 e 必须传，堆栈才完整
        return ResultUtil.getFailBean("系统异常，请稍后重试");
    }
}