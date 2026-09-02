package com.example.manage.config;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2026/9/2 19:32
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注接口允许访问的角色。
 * 用法：@RequireRole("admin") 表示只有管理员能调用。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String value();
}