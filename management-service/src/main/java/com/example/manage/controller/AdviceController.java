package com.example.manage.controller;

import com.example.manage.bean.AdviceBean;
import com.example.manage.bean.ClientBannerBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.mapper.AdviceMapper;
import com.example.manage.mapper.ClientBannerMapper;
import com.example.manage.service.AdviceService;
import com.example.manage.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/advice")
public class AdviceController {

    @Resource
    AdviceService adviceService;

    @RequestMapping("/addAdvice")
    @ResponseBody
    public ResultBean addAdvice(@RequestBody AdviceBean adviceBean) {
        adviceBean.setTime(System.currentTimeMillis());
//        int count = adviceMapper.addAdvice(adviceBean);
        if (adviceService.addAdvice(adviceBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/getAdvice")
    @ResponseBody
    public ResultBean getAdvice() {
        List<AdviceBean> advice = adviceService.getAdvice();
        return ResultUtil.getSuccessBean(advice);
    }
}
