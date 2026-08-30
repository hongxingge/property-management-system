package com.example.manage.service;

import com.example.manage.bean.CarportBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarportService {
    boolean addCarport(CarportBean carportBean);

    boolean updateCarport(CarportBean carportBean);

    boolean deleteCarport(int id);

    List<CarportBean> getCarport();

    CarportBean getCarportByNumber(String number);
}
