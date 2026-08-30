package com.example.manage.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingChargeMapper {
    int updateParkingCharge(float  parkingCharge);

    float getParkingCharge();
}
