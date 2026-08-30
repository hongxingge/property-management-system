package com.example.manage.service.imp;

import com.example.manage.bean.AdviceBean;
import com.example.manage.mapper.AdviceMapper;
import com.example.manage.service.AdviceService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class AdviceServiceImp implements AdviceService {

    @Resource
    AdviceMapper adviceMapper;

    @Override
    public boolean addAdvice(AdviceBean adviceBean) {
        int count = adviceMapper.addAdvice(adviceBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<AdviceBean> getAdvice() {
        return adviceMapper.getAdvice();
    }
}
