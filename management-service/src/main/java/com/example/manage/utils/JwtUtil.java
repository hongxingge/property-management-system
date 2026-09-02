package com.example.manage.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * JWT 工具类：负责生成和校验 token。
 *
 * 核心概念：
 * - 生成 token 时，用密钥对内容做 HMAC 签名；
 * - 校验 token 时，重新计算签名并比对，签名不一致说明被篡改，直接抛异常。
 */
public class JwtUtil {

    // 签名密钥（HS256 要求至少 32 字节；生产环境应放到配置/环境变量，不要写死在代码里）
    private static final String SECRET = "property-management-system-secret-key-2026";
    // token 有效期：7 天
    private static final long EXPIRE_MILLIS = 7L * 24 * 60 * 60 * 1000;

    private static Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成 token
     *
     * @param uid  用户唯一标识（管理员是 uid，业主是 id）
     * @param name 用户名
     * @param role 角色：admin（管理员）/ owner（业主），供后续 RBAC 鉴权使用
     */
    public static String generateToken(String uid, String name, String role) {
        return Jwts.builder()
                .setSubject(uid)                          // 存用户标识
                .claim("name", name)                      // 存用户名
                .claim("role", role)                      // 存角色
                .setIssuedAt(new Date())                  // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_MILLIS)) // 过期时间
                .signWith(getKey(), SignatureAlgorithm.HS256) // 用密钥签名
                .compact();
    }

    /**
     * 解析并验证 token，返回其中的 Claims。
     * token 无效、被篡改或已过期都会抛出异常（由拦截器统一捕获处理）。
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
