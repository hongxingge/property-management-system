package com.example.manage.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置：注册 JWT 拦截器。
 *
 * 拦截规则：所有接口都要带合法 token，除了下面几个放行的公开接口。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",        // 管理员登录
                        "/roomUser/login",    // 业主登录
                        "/user_images/**",    // 图片展示（<img> 标签无法带 token，必须放行）
                        "/fileUpload",        // 文件上传（后续阶段可收紧）
                        "/error",             // Spring 错误页
                        "/payCost/notify",    // 支付宝异步回调(支付宝服务器调用,无 token)
                        "/payCost/return"     // 支付宝同步返回(浏览器跳转,无自定义 header)
                );
    }
}
