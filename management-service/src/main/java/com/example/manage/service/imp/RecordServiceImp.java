package com.example.manage.service.imp;

import com.example.manage.bean.RecordBean;
import com.example.manage.mapper.RecordMapper;
import com.example.manage.service.RecordService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class RecordServiceImp implements RecordService {

    @Resource
    RecordMapper recordMapper;

    @Override
    public boolean addRecord(RecordBean recordBean) {
        int count = recordMapper.addRecord(recordBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRecord(RecordBean recordBean) {
        int count = recordMapper.updateRecord(recordBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRecord(int id) {
        int count = recordMapper.deleteRecord(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<RecordBean> getRecord() {
        return recordMapper.getRecord();
    }

    @Override
    public List<RecordBean> getRecordByCondition(String condition) {
        return recordMapper.getRecordByCondition(condition);
    }
}
