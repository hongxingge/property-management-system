package com.example.manage.mapper;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.ParkBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkMapper {

    int addPark(ParkBean parkBean);

    int updatePark(ParkBean parkBean);

    int deletePark(int id);

    List<ParkBean> getPark();

    ParkBean getParkById(int id);

    List<ParkBean> getParkByNumber(String number);

}
