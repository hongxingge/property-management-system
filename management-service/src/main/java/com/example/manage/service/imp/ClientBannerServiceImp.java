package com.example.manage.service.imp;

import com.example.manage.bean.ClientBannerBean;
import com.example.manage.mapper.ClientBannerMapper;
import com.example.manage.service.ClientBannerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ClientBannerServiceImp implements ClientBannerService {

    private static final String BANNER_CACHE_KEY = "property:banner:list";

    private static final long CACHE_TTL = 30;

    @Resource
    ClientBannerMapper clientBannerMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean addBanner(ClientBannerBean deviceBean) {

        int count = clientBannerMapper.addBanner(deviceBean);

        if (count > 0){
            //新增成功,删除就缓存
            stringRedisTemplate.delete(BANNER_CACHE_KEY);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBanner(ClientBannerBean deviceBean) {

        int count = clientBannerMapper.updateBanner(deviceBean);

        if (count > 0){
            // 更新成功后，删除旧缓存
            stringRedisTemplate.delete(BANNER_CACHE_KEY);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBanner(Long id) {
        int count = clientBannerMapper.deleteBanner(id);

        if (count > 0){
            // 删除成功后，删除旧缓存
            stringRedisTemplate.delete(BANNER_CACHE_KEY);
            return true;
        }
        return false;
    }

    @Override
    public List<ClientBannerBean> getBanner() {
        //1.查询redis
        String json = stringRedisTemplate.opsForValue().get(BANNER_CACHE_KEY);

        if(json != null){
            try{
                return objectMapper.readValue(json, new TypeReference<List<ClientBannerBean>>() {}
                );
            }catch (Exception e) {
                // Redis 中数据反序列化失败
                // 删除异常缓存，然后继续查询 MySQL
                stringRedisTemplate.delete(BANNER_CACHE_KEY);
            }
        }

        // 2. Redis 没有 → 查询 MySQL
        List<ClientBannerBean> banners = clientBannerMapper.getBanner();

        // 3. MySQL 查询成功 → 写入 Redis
        if(banners != null && !banners.isEmpty()){
            try{
                String cacheJson = objectMapper.writeValueAsString(banners);

                stringRedisTemplate.opsForValue().set(
                        BANNER_CACHE_KEY,
                        cacheJson,
                        CACHE_TTL,
                        TimeUnit.MINUTES
                );
            }catch (Exception e) {
                // Redis 写入失败不应该影响正常业务
                // 这里暂时打印异常
                e.printStackTrace();
            }
        }

        // 4. 返回 MySQL 查询结果
        return banners;
    }
}
