package com.example.manage.service;

import com.example.manage.bean.FaultRepairs;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FaultRepairsService {
    boolean addFault(FaultRepairs faultRepairs);

    boolean updateFaultForClient(FaultRepairs faultRepairs);

    boolean updateFaultForAdmin(int state, int id);

    boolean deleteFault(int id);

    List<FaultRepairs> getFault();

    List<FaultRepairs> getFaultByUid(String uid);
}
