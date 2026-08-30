package com.example.manage.mapper;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.RecordBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordMapper {

    int addRecord(RecordBean recordBean);

    int updateRecord(RecordBean recordBean);

    int deleteRecord(int id);

    List<RecordBean> getRecord();

    List<RecordBean> getRecordByCondition(String condition);

}
