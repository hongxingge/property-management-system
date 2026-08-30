package com.example.manage.service.imp;

import com.example.manage.bean.ClientBannerBean;
import com.example.manage.mapper.ClientBannerMapper;
import com.example.manage.service.ClientBannerService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class ClientBannerServiceImp implements ClientBannerService {

    @Resource
    ClientBannerMapper clientBannerMapper;

    @Override
    public boolean addBanner(ClientBannerBean deviceBean) {
        int count = clientBannerMapper.addBanner(deviceBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBanner(ClientBannerBean deviceBean) {
        int count = clientBannerMapper.updateBanner(deviceBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBanner(Long id) {
        int count = clientBannerMapper.deleteBanner(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<ClientBannerBean> getBanner() {
        return clientBannerMapper.getBanner();
    }
}
