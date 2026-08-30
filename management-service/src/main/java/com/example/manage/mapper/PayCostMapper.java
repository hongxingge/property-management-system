package com.example.manage.mapper;

import com.example.manage.bean.ClientBannerBean;
import com.example.manage.bean.PayCostBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayCostMapper {

    int addCost(PayCostBean payCostBean);

    int pay(int id);

    int updateCost(PayCostBean payCostBean);

    int deleteCost(int id);

    List<PayCostBean> getCost();

   PayCostBean getCostById(int costId);

    List<PayCostBean> getCostByUid(String uid);
}
