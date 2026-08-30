package com.example.manage.service;

import com.example.manage.bean.ParkRentBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParkRentService {
    boolean addRent(ParkRentBean parkRentBean);

    boolean updateRent(ParkRentBean parkRentBean);

    boolean deleteRent(int id);

    List<ParkRentBean> getRent();

    List<ParkRentBean> getRentByOwnerId(String ownerId);

    List<ParkRentBean> getRentByParkNumber(String number);

    List<ParkRentBean> getRentByCondition(ParkRentBean parkRentBean);

}
