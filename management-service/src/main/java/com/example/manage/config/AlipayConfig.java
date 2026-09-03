package com.example.manage.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 * @description:  支付宝客户端配置:把 yml 里的 6 个参数组装成一个全局 AlipayClient 单例。
 *                AlipayClient 线程安全,整个应用共享一个即可。
 * @date 2026/9/3 19:44
 */
@Configuration
public class AlipayConfig {
    @Value("${alipay.app-id}")        private String appId;
    @Value("${alipay.app-private-key}") private String appPrivateKey;
    @Value("${alipay.alipay-public-key}") private String alipayPublicKey;
    @Value("${alipay.gateway}")       private String gateway;
    @Value("${alipay.charset}")       private String charset;
    @Value("${alipay.sign-type}")     private String signType;

    @Bean
    public AlipayClient alipayClient() {
        // 参数顺序:网关, appId, 应用私钥, 格式, 编码, 支付宝公钥, 签名算法
        return new DefaultAlipayClient(
                gateway, appId, appPrivateKey, "json", charset, alipayPublicKey, signType);
    }
}