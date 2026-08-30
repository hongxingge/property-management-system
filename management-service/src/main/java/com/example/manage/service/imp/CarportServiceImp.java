package com.example.manage.service.imp;

import com.example.manage.bean.CarportBean;
import com.example.manage.mapper.CarportMapper;
import com.example.manage.service.CarportService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CarportServiceImp implements CarportService {

    @Resource
    CarportMapper carportMapper;

    @Override
    public boolean addCarport(CarportBean carportBean) {
        int count = carportMapper.addCarport(carportBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCarport(CarportBean carportBean) {
        int count = carportMapper.updateCarport(carportBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCarport(int id) {
        int count = carportMapper.deleteCarport(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<CarportBean> getCarport() {
        return carportMapper.getCarport();
    }

    @Override
    public CarportBean getCarportByNumber(String number) {
        CarportBean bean = carportMapper.getCarportByNumber(number);
        return bean;
    }
}
