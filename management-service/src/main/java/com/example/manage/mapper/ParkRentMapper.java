package com.example.manage.mapper;

import com.example.manage.bean.OwnerCarBean;
import com.example.manage.bean.ParkRentBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkRentMapper {

    int addRent(ParkRentBean parkRentBean);

    int updateRent(ParkRentBean parkRentBean);

    List<ParkRentBean> getRent();

    List<ParkRentBean> getRentByOwnerId(String ownerId);

    List<ParkRentBean> getRentByParkNumber(String number);

    List<ParkRentBean> getRentByCondition(ParkRentBean parkRentBean);

    int deleteRent(int id);
}
