package com.example.manage.controller;

import com.example.manage.bean.OwnerCarBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.service.OwnerCarService;
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
@RequestMapping("/ownerCar")
public class OwnerCarController {

    @Resource
    OwnerCarService ownerCarService;

    @RequestMapping("/addOwnerCar")
    @ResponseBody
    public ResultBean addOwnerCar(@RequestBody OwnerCarBean ownerCarBean) {
        List<OwnerCarBean> cars = ownerCarService.getOwnerCarByNumber(ownerCarBean.getCarNumber());
        if (cars != null && cars.size()>0) {
          return   ResultUtil.getFailBean("该车牌号已存在，请修改后提交");
        }
        if (ownerCarService.addOwnerCar(ownerCarBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateOwnerCar")
    @ResponseBody
    public ResultBean updateOwnerCar(@RequestBody OwnerCarBean ownerCarBean) {
        if (ownerCarService.updateOwnerCar(ownerCarBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteOwnerCar")
    @ResponseBody
    public ResultBean deleteOwnerCar(@RequestBody Map<String, Integer> params) {
        if (ownerCarService.deleteOwnerCar(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getOwnerCar")
    @ResponseBody
    public ResultBean getOwnerCar() {
        List<OwnerCarBean> ownerCarBeans = ownerCarService.getOwnerCar();
        return ResultUtil.getSuccessBean(ownerCarBeans);
    }

    @RequestMapping("/getOwnerCarByNumber")
    @ResponseBody
    public ResultBean getOwnerCarByNumber(@RequestParam("number") String number) {
        List<OwnerCarBean> ownerCarBeans = ownerCarService.getOwnerCarByNumber(number);
        return ResultUtil.getSuccessBean(ownerCarBeans);
    }

    @RequestMapping("/getOwnerCarByOwnerId")
    @ResponseBody
    public ResultBean getOwnerCarByOwnerId(@RequestParam("ownerId") String ownerId) {
        List<OwnerCarBean> ownerCarBeans = ownerCarService.getOwnerCarByOwnerId(ownerId);
        return ResultUtil.getSuccessBean(ownerCarBeans);
    }

}
