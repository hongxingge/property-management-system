package com.example.manage.service;

import com.example.manage.bean.PayCostBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PayCostService {
    boolean addCost(PayCostBean payCostBean);

    boolean pay(int id);

    boolean updateCost(PayCostBean payCostBean);

    boolean deleteCost(int id);

    List<PayCostBean> getCost();

    PayCostBean getCostById(int costId);

    List<PayCostBean> getCostByUid(String uid);
}
