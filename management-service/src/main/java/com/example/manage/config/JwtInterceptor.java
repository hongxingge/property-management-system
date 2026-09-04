package com.example.manage.config;

import com.example.manage.service.TokenBlacklistService;
import com.example.manage.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT 认证拦截器：拦截所有请求，校验请求头里的 token，无效则拒绝。
 *
 * 校验逻辑：
 * 1. 从请求头 Authorization 里取出 "Bearer xxx" 中的 token；
 * 2. 用 JwtUtil 解析验证（签名/过期校验）；
 * 3. 通过则把用户信息放入 request 属性，放行；失败则返回 401 JSON。
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private TokenBlacklistService tokenBlacklistService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);

                // ★ 黑名单校验:改密码后旧 token 在这里被拦下
                if (tokenBlacklistService.isBlacklisted(token)) {
                    writeJson(response, "{\"errCode\":401,\"errMsg\":\"登录已失效，请重新登录\",\"data\":null}");
                    return false;
                }

                request.setAttribute("uid", claims.getSubject());
                request.setAttribute("role", claims.get("role"));

                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
                    if (requireRole != null) {
                        String role = (String) claims.get("role");
                        if (!requireRole.value().equals(role)) {
                            writeJson(response, "{\"errCode\":403,\"errMsg\":\"无权限访问\",\"data\":null}");
                            return false;
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();   // ← 加这行，把真实异常打到控制台
                writeJson(response, "{\"errCode\":401,\"errMsg\":\"登录已失效，请重新登录\",\"data\":null}");
                return false;
            }
        }
        writeJson(response, "{\"errCode\":401,\"errMsg\":\"未登录\",\"data\":null}");
        return false;
    }

    private void writeJson(HttpServletResponse response, String json) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
