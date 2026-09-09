package com.example.manage.exception;

/**
 * @author Administrator
 * @version 1.0
 * @description: 业务异常：表示「业务层已知的错误」。
 *  *
 *  * 什么时候用它？
 *  * - 比如"账单不存在""密码不正确""知识库调用失败"这类业务上能预期的错误
 *  * - 这些错误不该落到兜底的 Exception 处理（否则前端只会看到笼统的"系统异常"）
 *  * - 抛 BizException 时带上明确的 message，全局异常处理器会把这个 message 原样返回给前端
 *  *
 *  * 为什么继承 RuntimeException 而不是 Exception？
 *  * - RuntimeException 是非受检异常，调用方不用强制写 throws 或 try-catch
 *  * - 业务代码里可以随时 throw new BizException("xxx")，不会污染方法签名
 * @date 2026/9/8 20:57
 */
public class BizException extends RuntimeException{

    public BizException(String message){
        super(message);
    }

    // 带 cause 的构造器:有时你想把底层异常(比如 SQL 异常)也带上,方便排查
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}