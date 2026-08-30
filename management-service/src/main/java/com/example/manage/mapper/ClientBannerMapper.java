package com.example.manage.mapper;

import com.example.manage.bean.ClientBannerBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClientBannerMapper {

    int addBanner(ClientBannerBean deviceBean);

    int updateBanner(ClientBannerBean deviceBean);

    int deleteBanner(Long id);

    List<ClientBannerBean> getBanner();
}
