package com.example.manage.service.imp;

import com.example.manage.bean.ParkRentBean;
import com.example.manage.mapper.ParkRentMapper;
import com.example.manage.service.ParkRentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ParkRentServiceImp implements ParkRentService {

    @Resource
    ParkRentMapper parkRentMapper;

    @Override
    public boolean addRent(ParkRentBean parkRentBean) {
        int count = parkRentMapper.addRent(parkRentBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRent(ParkRentBean parkRentBean) {
        int count = parkRentMapper.updateRent(parkRentBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRent(int id) {
        int count = parkRentMapper.deleteRent(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<ParkRentBean> getRent() {
        return parkRentMapper.getRent();
    }

    @Override
    public List<ParkRentBean> getRentByOwnerId(String ownerId) {
        return parkRentMapper.getRentByOwnerId(ownerId);
    }

    @Override
    public List<ParkRentBean> getRentByParkNumber(String number) {
        return parkRentMapper.getRentByParkNumber(number);
    }

    @Override
    public List<ParkRentBean> getRentByCondition(ParkRentBean parkRentBean) {
        return parkRentMapper.getRentByCondition(parkRentBean);
    }
}
