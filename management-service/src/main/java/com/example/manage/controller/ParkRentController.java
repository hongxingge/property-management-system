package com.example.manage.controller;

import com.example.manage.bean.*;
import com.example.manage.config.RequireRole;
import com.example.manage.mapper.*;
import com.example.manage.service.*;
import com.example.manage.utils.ResultUtil;
import com.example.manage.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rent")
public class ParkRentController {

    @Autowired
    PayCostMapper payCostMapper;

    @Resource
    ParkService parkService;

    @Resource
    OwnerCarService ownerCarService;;

    @Resource
    RoomUserService roomUserService;;

    @Resource
    ParkRentService parkRentService;;

    @Resource
    PayCostService payCostService;;

    @Resource
    ParkingChargeService parkingChargeService;;

    @RequestMapping("/getRent")
    @ResponseBody
    public ResultBean getRent() {
        List<ParkRentBean> parkBeans = parkRentService.getRent();
        for (ParkRentBean parkBean : parkBeans) {
            parkBean.setParkBean(parkService.getParkById(parkBean.getParkId()));
            parkBean.setOwnerCarBean(ownerCarService.getCarById(parkBean.getCarId()));
            parkBean.setRoomUserBean(roomUserService.getRoomUserById(parkBean.getOwnerId()));
            parkBean.setPayCostBean(payCostService.getCostById(parkBean.getCostId()));
        }
        return ResultUtil.getSuccessBean(parkBeans);
    }

    @RequestMapping("/getRentByOwnerId")
    @ResponseBody
    public ResultBean getRentByOwnerId(@RequestParam("ownerId") String ownerId) {
        List<ParkRentBean> parkBeans = parkRentService.getRentByOwnerId(ownerId);
        for (ParkRentBean parkBean : parkBeans) {

            parkBean.setParkBean(parkService.getParkById(parkBean.getParkId()));
            parkBean.setOwnerCarBean(ownerCarService.getCarById(parkBean.getCarId()));
            parkBean.setRoomUserBean(roomUserService.getRoomUserById(parkBean.getOwnerId()));
            parkBean.setPayCostBean(payCostService.getCostById(parkBean.getCostId()));
        }
        return ResultUtil.getSuccessBean(parkBeans);
    }

    @RequestMapping("/getRentByParkNumber")
    @ResponseBody
    public ResultBean getRentByParkNumber(@RequestParam("number") String number) {
        List<ParkRentBean> parkBeans = parkRentService.getRentByParkNumber(number);
        for (ParkRentBean parkBean : parkBeans) {
            parkBean.setParkBean(parkService.getParkById(parkBean.getParkId()));
            parkBean.setOwnerCarBean(ownerCarService.getCarById(parkBean.getCarId()));
            parkBean.setRoomUserBean(roomUserService.getRoomUserById(parkBean.getOwnerId()));
            parkBean.setPayCostBean(payCostService.getCostById(parkBean.getCostId()));
        }
        return ResultUtil.getSuccessBean(parkBeans);
    }

    @RequestMapping("/addRent")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean addOwnerCar(@RequestBody ParkRentBean parkRentBean) {
        List<ParkRentBean> list = parkRentService.getRentByCondition(parkRentBean);
        if (list != null && list.size() > 0) {
            return ResultUtil.getFailBean("车辆或时间冲突，请检查后再次添加");
        }

//            2023-02-07
        long startTime = TimeUtil.getStringToDate(parkRentBean.getStartTime());
        long endTime = TimeUtil.getStringToDate(parkRentBean.getEndTime());
        int dayCount = (int) (((endTime - startTime) / 1000 / 60 / 60 / 24) + 1);
        PayCostBean payCostBean = new PayCostBean();
        payCostBean.setTime(System.currentTimeMillis());
        payCostBean.setPayState(1);
        payCostBean.setIsCheck(1);
        payCostBean.setUid(parkRentBean.getOwnerId());
        payCostBean.setPayType(2);
        payCostBean.setPayMoney(dayCount * parkingChargeService.getParkingCharge());
        int id = payCostMapper.addCost(payCostBean);
        parkRentBean.setCostId(payCostBean.getId());
        if (parkRentService.addRent(parkRentBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateRent")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean updateRent(@RequestBody ParkRentBean parkRentBean) {
        List<ParkRentBean> list = parkRentService.getRentByCondition(parkRentBean);
        Iterator<ParkRentBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == parkRentBean.getId()) {
                iterator.remove();
            }
        }
        if (list != null && list.size() > 0) {
            return ResultUtil.getFailBean("车辆或时间冲突，请检查后再次添加");
        }
        if (parkRentService.updateRent(parkRentBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }


    @RequestMapping("/deleteRent")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean deleteRent(@RequestBody Map<String, Integer> params) {
        if (parkRentService.deleteRent(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

}
