package com.example.manage.service.imp;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.ClientBannerBean;
import com.example.manage.bean.ClientNewsBean;
import com.example.manage.mapper.ClientBannerMapper;
import com.example.manage.mapper.ClientNewsMapper;
import com.example.manage.service.ClientBannerService;
import com.example.manage.service.ClientNewsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class ClientNewsServiceImp implements ClientNewsService {

    @Resource
    ClientNewsMapper newsMapper;

    @Override
    public boolean addNews(ClientNewsBean newsBean) {
        int count = newsMapper.addNews(newsBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateNews(ClientNewsBean newsBean) {
        int count = newsMapper.updateNews(newsBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteNews(Long id) {
        int count = newsMapper.deleteNews(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public ClientNewsBean getNewsById(Long id) {
        ClientNewsBean bean = newsMapper.getNewsById(id);
        return bean;
    }

    @Override
    public List<ClientNewsBean> getNews() {
        return newsMapper.getNews();
    }
}
