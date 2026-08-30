package com.example.manage.mapper;

import com.example.manage.bean.AdviceBean;
import com.example.manage.bean.FaultRepairs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdviceMapper {

    int addAdvice(AdviceBean adviceBean);

    List<AdviceBean> getAdvice();

}
