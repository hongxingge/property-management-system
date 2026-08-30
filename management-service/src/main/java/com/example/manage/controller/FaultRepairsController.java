package com.example.manage.controller;

import com.example.manage.bean.ClientNewsBean;
import com.example.manage.bean.FaultRepairs;
import com.example.manage.bean.PayCostBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.mapper.FaultRepairsMapper;
import com.example.manage.mapper.PayCostMapper;
import com.example.manage.service.FaultRepairsService;
import com.example.manage.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/fault")
public class FaultRepairsController {

    @Resource
    FaultRepairsService repairsService;

    @RequestMapping("/addFault")
    @ResponseBody
    public ResultBean addFault(@RequestBody FaultRepairs faultRepairs) {
        faultRepairs.setTime(System.currentTimeMillis());
        faultRepairs.setState(1);
        if (repairsService.addFault(faultRepairs)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateFaultForClient")
    @ResponseBody
    public ResultBean updateFaultForClient(@RequestBody FaultRepairs faultRepairs) {
        if (repairsService.updateFaultForClient(faultRepairs)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/updateFaultForAdmin")
    @ResponseBody
    public ResultBean updateFaultForAdmin(@RequestBody Map<String, Integer> params) {
        if (repairsService.updateFaultForAdmin(params.get("state"),params.get("id"))){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteFault")
    @ResponseBody
    public ResultBean deleteFault(@RequestBody Map<String, Integer> params) {
        if (repairsService.deleteFault(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getFault")
    @ResponseBody
    public ResultBean getFault() {
        List<FaultRepairs> faultRepairs = repairsService.getFault();
        return ResultUtil.getSuccessBean(faultRepairs);
    }

    @RequestMapping("/getFaultByUid")
    @ResponseBody
    public ResultBean getFaultByUid(@RequestParam("uid") String uid) {
        List<FaultRepairs> faultRepairs = repairsService.getFaultByUid(uid);
        return ResultUtil.getSuccessBean(faultRepairs);
    }
}
