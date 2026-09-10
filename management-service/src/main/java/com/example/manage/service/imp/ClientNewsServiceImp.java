package com.example.manage.service.imp;

import com.example.manage.bean.ClientNewsBean;
import com.example.manage.mapper.ClientNewsMapper;
import com.example.manage.service.ClientNewsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class ClientNewsServiceImp implements ClientNewsService {

    // 缓存 key + 过期时间(30分钟)
    private static final String NEWS_CACHE_KEY = "property:news:list";
    private static final long CACHE_TTL = 30;

    @Resource
    ClientNewsMapper newsMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(ClientNewsServiceImp.class);

    @Override
    public boolean addNews(ClientNewsBean newsBean) {
        int count = newsMapper.addNews(newsBean);
        if (count > 0) {
            // 数据变了,删缓存,下次查询会重新从 MySQL 加载
            stringRedisTemplate.delete(NEWS_CACHE_KEY);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateNews(ClientNewsBean newsBean) {
        int count = newsMapper.updateNews(newsBean);
        if (count > 0) {
            stringRedisTemplate.delete(NEWS_CACHE_KEY);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteNews(Long id) {
        int count = newsMapper.deleteNews(id);
        if (count > 0) {
            stringRedisTemplate.delete(NEWS_CACHE_KEY);
            return true;
        }
        return false;
    }

    @Override
    public ClientNewsBean getNewsById(Long id) {
        // 详情查询暂不加缓存(保持简单),只缓存列表
        return newsMapper.getNewsById(id);
    }

    @Override
    public List<ClientNewsBean> getNews() {
        // 1. 先查 Redis,命中直接返回
        String json = stringRedisTemplate.opsForValue().get(NEWS_CACHE_KEY);
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<List<ClientNewsBean>>() {});
            } catch (Exception e) {
                stringRedisTemplate.delete(NEWS_CACHE_KEY);
            }
        }

        // 2. 加互斥锁防「缓存击穿」:只有一个请求能进查 MySQL,其他请求等待
        synchronized (this) {
            // 双重检查:拿到锁后,可能别的请求已经重建了缓存
            json = stringRedisTemplate.opsForValue().get(NEWS_CACHE_KEY);
            if (json != null) {
                try {
                    return objectMapper.readValue(json, new TypeReference<List<ClientNewsBean>>() {});
                } catch (Exception e) {
                    stringRedisTemplate.delete(NEWS_CACHE_KEY);
                }
            }

            // 3. 查 MySQL
            List<ClientNewsBean> news = newsMapper.getNews();

            // 4. 写缓存,同时处理「穿透」和「雪崩」
            if (news != null) {
                // 防穿透:空列表也缓存,但过期时间短(5分钟),避免"查不到"每次打库
                long ttl = news.isEmpty() ? 5 : CACHE_TTL;
                // 防雪崩:过期时间加 0~5 分钟随机值,避免大量 key 同时过期
                ttl = ttl + new Random().nextInt(5);
                try {
                    stringRedisTemplate.opsForValue().set(
                            NEWS_CACHE_KEY,
                            objectMapper.writeValueAsString(news),
                            ttl,
                            TimeUnit.MINUTES
                    );
                } catch (Exception e) {
                    log.warn("Redis 缓存写入失败", e);
                }
            }

            // 5. 返回结果
            return news;
        }
    }
}
