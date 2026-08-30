package com.example.manage.service.imp;

import com.example.manage.bean.OwnerCarBean;
import com.example.manage.mapper.OwnerCarMapper;
import com.example.manage.service.OwnerCarService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OwnerCarServiceImp implements OwnerCarService {

    @Resource
    OwnerCarMapper ownerCarMapper;

    @Override
    public boolean addOwnerCar(OwnerCarBean ownerCarBean) {
        int count = ownerCarMapper.addOwnerCar(ownerCarBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateOwnerCar(OwnerCarBean ownerCarBean) {
        int count = ownerCarMapper.updateOwnerCar(ownerCarBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteOwnerCar(int id) {
        int count = ownerCarMapper.deleteOwnerCar(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<OwnerCarBean> getOwnerCar() {
        return ownerCarMapper.getOwnerCar();
    }

    @Override
    public List<OwnerCarBean> getOwnerCarByNumber(String number) {
        return ownerCarMapper.getOwnerCarByNumber(number);
    }

    @Override
    public List<OwnerCarBean> getOwnerCarByOwnerId(String ownerId) {
        return ownerCarMapper.getOwnerCarByOwnerId(ownerId);
    }

    @Override
    public OwnerCarBean getCarById(int id) {
        return ownerCarMapper.getCarById(id);
    }
}
