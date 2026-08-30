package com.example.manage.service;

import com.example.manage.bean.ClientBannerBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientBannerService {
    boolean addBanner(ClientBannerBean deviceBean);

    boolean updateBanner(ClientBannerBean deviceBean);

    boolean deleteBanner(Long id);

    List<ClientBannerBean> getBanner();
}
