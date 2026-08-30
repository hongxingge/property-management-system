package com.example.manage.service;

import com.example.manage.bean.AdviceBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdviceService {

    boolean addAdvice(AdviceBean adviceBean);

    List<AdviceBean> getAdvice();
}
