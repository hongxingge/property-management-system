package com.example.manage.service.imp;

import com.example.manage.bean.ParkBean;
import com.example.manage.mapper.ParkMapper;
import com.example.manage.service.ParkService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class ParkServiceImp implements ParkService {

    @Resource
    ParkMapper parkMapper;

    @Override
    public boolean addPark(ParkBean parkBean) {
        int count = parkMapper.addPark(parkBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePark(ParkBean parkBean) {
        int count = parkMapper.updatePark(parkBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePark(int id) {
        int count = parkMapper.deletePark(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<ParkBean> getPark() {
        return parkMapper.getPark();
    }

    @Override
    public ParkBean getParkById(int id) {
        return parkMapper.getParkById(id);
    }

    @Override
    public List<ParkBean> getParkByNumber(String number) {
        return parkMapper.getParkByNumber(number);
    }
}
