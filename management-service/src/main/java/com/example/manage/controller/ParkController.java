package com.example.manage.controller;

import com.example.manage.bean.ParkBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.config.RequireRole;
import com.example.manage.service.ParkService;
import com.example.manage.utils.ResultUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/park")
public class  ParkController {

    @Resource
    ParkService parkService;

    @RequestMapping("/addPark")
    @ResponseBody
    public ResultBean addPark(@Valid @RequestBody ParkBean parkBean) {
        List<ParkBean> beans = parkService.getParkByNumber(parkBean.getNumber());
        if (beans != null && beans.size()>0) {
            return ResultUtil.getFailBean("该编号已存在，请修改后提交");
        }
        if (parkService.addPark(parkBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updatePark")
    @ResponseBody
    public ResultBean updatePark(@Valid@RequestBody ParkBean parkBean) {
        if (parkService.updatePark(parkBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deletePark")
    @ResponseBody
    public ResultBean deletePark(@Valid@RequestBody Map<String, Integer> params) {
        if (parkService.deletePark(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getPark")
    @ResponseBody
    public ResultBean getPark() {
        List<ParkBean> parkBeans = parkService.getPark();
        return ResultUtil.getSuccessBean(parkBeans);
    }

    @RequestMapping("/getParkByNumber")
    @ResponseBody
    public ResultBean getParkByNumber(@RequestParam("number") String number) {
        return ResultUtil.getSuccessBean(parkService.getParkByNumber(number));
    }

}
