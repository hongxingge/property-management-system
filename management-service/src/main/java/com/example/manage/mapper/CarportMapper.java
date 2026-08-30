package com.example.manage.mapper;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarportMapper {

    int addCarport(CarportBean carportBean);

    int updateCarport(CarportBean carportBean);

    int deleteCarport(int id);

    List<CarportBean> getCarport();

    CarportBean getCarportByNumber(String number);

}
