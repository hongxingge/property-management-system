package com.example.manage.service;

import com.example.manage.bean.ParkBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParkService {
    boolean addPark(ParkBean parkBean);

    boolean updatePark(ParkBean parkBean);

    boolean deletePark(int id);

    List<ParkBean> getPark();

    ParkBean getParkById(int id);

    List<ParkBean> getParkByNumber(String number);
}
