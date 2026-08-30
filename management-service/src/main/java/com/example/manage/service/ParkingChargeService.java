package com.example.manage.service;

import org.springframework.stereotype.Service;

@Service
public interface ParkingChargeService {
    boolean updateParkingCharge(float  parkingCharge);

    float getParkingCharge();
}
