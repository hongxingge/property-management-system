package com.example.manage.mapper;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.OwnerCarBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OwnerCarMapper {

    int addOwnerCar(OwnerCarBean ownerCarBean);

    int updateOwnerCar(OwnerCarBean ownerCarBean);

    int deleteOwnerCar(int id);

    List<OwnerCarBean> getOwnerCar();

    List<OwnerCarBean> getOwnerCarByNumber(String number);

    List<OwnerCarBean> getOwnerCarByOwnerId(String ownerId);

     OwnerCarBean  getCarById( int id);

}
