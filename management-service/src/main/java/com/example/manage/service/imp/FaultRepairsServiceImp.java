package com.example.manage.service.imp;

import com.example.manage.bean.FaultRepairs;
import com.example.manage.mapper.FaultRepairsMapper;
import com.example.manage.service.FaultRepairsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class FaultRepairsServiceImp implements FaultRepairsService {

    @Resource
    FaultRepairsMapper repairsMapper;

    @Override
    public boolean addFault(FaultRepairs faultRepairs) {
        int count = repairsMapper.addFault(faultRepairs);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFaultForClient(FaultRepairs faultRepairs) {
        int count = repairsMapper.updateFaultForClient(faultRepairs);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFaultForAdmin(int state, int id) {
        int count = repairsMapper.updateFaultForAdmin(state,id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFault(int id) {
        int count = repairsMapper.deleteFault(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<FaultRepairs> getFault() {
        return repairsMapper.getFault();
    }

    @Override
    public List<FaultRepairs> getFaultByUid(String uid) {
        return repairsMapper.getFaultByUid(uid);
    }
}
