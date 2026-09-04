package com.example.manage.service;

import com.example.manage.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @author Administrator
 * @version 1.0
 * @description:  token 黑名单:改密码/退出登录后,把旧 token 加入 Redis,让它立即失效。
 *                key 的过期时间 = token 剩余有效期,这样黑名单不会无限膨胀。
 * @date 2026/9/4 20:54
 */
@Service
public class TokenBlacklistService {
    private static final String PREFIX = "token:blacklist:";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /** 把 token 加入黑名单(过期时间设为 token 剩余有效期) */
    public void blacklist(String token) {
        Claims claims = JwtUtil.parseToken(token);
        Date expiration = claims.getExpiration();
        long remainSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;
        if (remainSeconds > 0) {
            stringRedisTemplate.opsForValue().set(PREFIX + token, "1", remainSeconds, TimeUnit.SECONDS);
        }
    }

    /** 判断 token 是否在黑名单里 */
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(PREFIX + token));
    }
}