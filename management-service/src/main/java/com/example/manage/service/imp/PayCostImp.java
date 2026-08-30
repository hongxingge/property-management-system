package com.example.manage.service.imp;

import com.example.manage.bean.PayCostBean;
import com.example.manage.mapper.ParkingChargeMapper;
import com.example.manage.mapper.PayCostMapper;
import com.example.manage.service.PayCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class PayCostImp implements PayCostService {

    @Resource
    PayCostMapper payCostMapper;

    @Override
    public boolean addCost(PayCostBean payCostBean) {
        int count = payCostMapper.addCost(payCostBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    //更新支付状态(支付)
    @Override
    public boolean pay(int id) {
        int count = payCostMapper.pay(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCost(PayCostBean payCostBean) {
        int count = payCostMapper.updateCost(payCostBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCost(int id) {
        int count = payCostMapper.deleteCost(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<PayCostBean> getCost() {
        return payCostMapper.getCost();
    }

    @Override
    public PayCostBean getCostById(int costId) {
        return  payCostMapper.getCostById(costId);
    }

    @Override
    public List<PayCostBean> getCostByUid(String uid) {
        return payCostMapper.getCostByUid(uid);
    }
}
