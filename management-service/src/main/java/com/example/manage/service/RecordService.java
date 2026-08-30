package com.example.manage.service;

import com.example.manage.bean.RecordBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {
    boolean addRecord(RecordBean recordBean);

    boolean updateRecord(RecordBean recordBean);

    boolean deleteRecord(int id);

    List<RecordBean> getRecord();

    List<RecordBean> getRecordByCondition(String condition);
}
