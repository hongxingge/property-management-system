package com.example.manage.controller;

import com.example.manage.bean.PayCostBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.config.RequireRole;
import com.example.manage.service.ParkingChargeService;
import com.example.manage.service.PayCostService;
import com.example.manage.utils.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payCost")
public class PayCostController {

    @Resource
    PayCostService payCostService;

    @Resource
    ParkingChargeService parkingChargeService;

    @RequestMapping("/updateParkingCharge")
    @ResponseBody
    public ResultBean updateParkingCharge(@RequestBody Map<String, Float> params) {
        if (parkingChargeService.updateParkingCharge(params.get("parkingCharge"))){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/getParkingCharge")
    @ResponseBody
    public ResultBean getParkingCharge() {
        float parkingCharge = parkingChargeService.getParkingCharge();
        return ResultUtil.getSuccessBean(parkingCharge, "获取成功");
    }


    @RequestMapping("/pay")
    @RequireRole("owner")
    @ResponseBody
    public ResultBean pay(@RequestBody Map<String, Integer> params) {
        if (payCostService.pay(params.get("id"))){
            return ResultUtil.getResultBean(1, "支付成功");
        }
        return ResultUtil.getResultBean(0, "支付失败");
    }

    @RequestMapping("/addCost")
    @ResponseBody
    public ResultBean addCost(@RequestBody PayCostBean payCostBean) {
        payCostBean.setTime(System.currentTimeMillis());
        payCostBean.setPayState(1);
        payCostBean.setIsCheck(1);
        if (payCostService.addCost(payCostBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateCost")
    @ResponseBody
    public ResultBean updateCost(@RequestBody PayCostBean payCostBean) {
        if (payCostService.updateCost(payCostBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteCost")
    @ResponseBody
    public ResultBean deleteCost(@RequestBody Map<String, Integer> params) {
        if (payCostService.deleteCost(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getCost")
    @ResponseBody
    public ResultBean getCost() {
        List<PayCostBean> payCostBeans = payCostService.getCost();
        return ResultUtil.getSuccessBean(payCostBeans);
    }

    @RequestMapping("/getCostByUid")
    @ResponseBody
    public ResultBean getCostByUid(@RequestParam("uid") String uid) {
        List<PayCostBean> payCostBeans = payCostService.getCostByUid(uid);
        return ResultUtil.getSuccessBean(payCostBeans);
    }
}
