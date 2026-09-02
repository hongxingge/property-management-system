package com.example.manage.controller;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.config.RequireRole;
import com.example.manage.service.CarportService;
import com.example.manage.utils.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carport")
public class CarportController {

    @Resource
    CarportService carportService;

    @RequestMapping("/addCarport")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean addCarport(@RequestBody CarportBean carportBean) {
        CarportBean bean = carportService.getCarportByNumber(carportBean.getNumber());
        if (bean != null) {
          return   ResultUtil.getFailBean("该编号已存在，请修改后提交");
        }
        if (carportService.addCarport(carportBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateCarport")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean updateCarport(@RequestBody CarportBean carportBean) {
        CarportBean bean = carportService.getCarportByNumber(carportBean.getNumber());
        if (bean != null) {
            return   ResultUtil.getFailBean("该编号已存在,请在该编号下修改");
        }
        if (carportService.updateCarport(carportBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteCarport")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean deleteCarport(@RequestBody Map<String, Integer> params) {
        if (carportService.deleteCarport(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getCarport")
    @ResponseBody
    public ResultBean getCarport() {
        List<CarportBean> carport = carportService.getCarport();
        return ResultUtil.getSuccessBean(carport);
    }
}
