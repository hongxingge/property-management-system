package com.example.manage.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Administrator
 * @version 1.0
 * @description:
 * 标注接口允许访问的角色。
 *   用法：
 *     @RequireRole(RoleConstant.ADMIN)                        // 单个角色
 *     @RequireRole({RoleConstant.ADMIN, RoleConstant.STAFF})   // 多个角色,满足其一即可
 * @date 2026/9/2 19:32
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String[] value();   // 从单个 String 改成 String[],支持一个接口允许多个角色
}