package com.example.manage.service.imp;

import com.example.manage.mapper.ParkingChargeMapper;
import com.example.manage.service.ParkingChargeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ParkingChargeServiceImp implements ParkingChargeService {

    @Resource
    ParkingChargeMapper parkingChargeMapper;

    @Override
    public boolean updateParkingCharge(float parkingCharge) {
        int count = parkingChargeMapper.updateParkingCharge(parkingCharge);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public float getParkingCharge() {
        return parkingChargeMapper.getParkingCharge();
    }
}
