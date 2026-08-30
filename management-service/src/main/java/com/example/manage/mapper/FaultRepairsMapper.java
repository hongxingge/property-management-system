package com.example.manage.mapper;

import com.example.manage.bean.FaultRepairs;
import com.example.manage.bean.PayCostBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaultRepairsMapper {

    int addFault(FaultRepairs faultRepairs);

    int updateFaultForClient(FaultRepairs faultRepairs);

    int updateFaultForAdmin(int state, int id);

    int deleteFault(int id);

    List<FaultRepairs> getFault();

    List<FaultRepairs> getFaultByUid(String uid);
}
