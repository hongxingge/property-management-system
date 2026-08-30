package com.example.manage.service;

import com.example.manage.bean.OwnerCarBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OwnerCarService {
    boolean addOwnerCar(OwnerCarBean ownerCarBean);

    boolean updateOwnerCar(OwnerCarBean ownerCarBean);

    boolean deleteOwnerCar(int id);

    List<OwnerCarBean> getOwnerCar();

    List<OwnerCarBean> getOwnerCarByNumber(String number);

    List<OwnerCarBean> getOwnerCarByOwnerId(String ownerId);

    OwnerCarBean  getCarById( int id);
}
