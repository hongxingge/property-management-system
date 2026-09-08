package com.example.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2026/9/8 15:41
 */
@Configuration
public class RedisConfig {

        @Bean
        public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory){
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(connectionFactory);

            return template;
        }
}