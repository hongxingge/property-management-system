package com.example.manage.config;

/**
 * @author Administrator
 * @version 1.0
 * @description:
 *   角色常量：把角色字符串集中管理，避免各处硬编码拼写错误。
 *   将来加「物业员工」角色时，在这里加一行 STAFF 即可。
 * @date 2026/9/9 19:20
 */
public final class RoleConstant {
    public static final String ADMIN = "admin";//管理员
    public static final String OWNER = "owner";//业主
    public static final String STAFF = "staff";//物业员工

    private RoleConstant(){

    }
}