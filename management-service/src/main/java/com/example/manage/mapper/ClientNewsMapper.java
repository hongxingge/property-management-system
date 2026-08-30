package com.example.manage.mapper;

import com.example.manage.bean.ClientNewsBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClientNewsMapper {
    int addNews(ClientNewsBean newsBean);

    int updateNews(ClientNewsBean newsBean);

    int deleteNews(Long id);

    ClientNewsBean getNewsById(Long id);

    List<ClientNewsBean> getNews();

}
