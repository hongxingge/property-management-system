package com.example.manage.service;

import com.example.manage.bean.ClientNewsBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientNewsService {
    boolean addNews(ClientNewsBean newsBean);

    boolean updateNews(ClientNewsBean newsBean);

    boolean deleteNews(Long id);

    ClientNewsBean getNewsById(Long id);

    List<ClientNewsBean> getNews();
}
