package com.example.manage.controller;

import com.example.manage.bean.RecordBean;
import com.example.manage.bean.RecordBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.mapper.RecordMapper;
import com.example.manage.mapper.RecordMapper;
import com.example.manage.service.RecordService;
import com.example.manage.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/record")
public class RecordController {

    @Autowired
    RecordMapper recordMapper;

    @Resource
    RecordService recordService;

    @RequestMapping("/addRecord")
    @ResponseBody
    public ResultBean addRecord(@RequestBody RecordBean recordBean) {
//        int count = recordMapper.addRecord(recordBean);
//        return ResultUtil.getResultBean(count, "添加失败");
        if (recordService.addRecord(recordBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateRecord")
    @ResponseBody
    public ResultBean updateRecord(@RequestBody RecordBean recordBean) {
//        int count = recordMapper.updateRecord(recordBean);
//        return ResultUtil.getResultBean(count, "修改失败");
        if (recordService.updateRecord(recordBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteRecord")
    @ResponseBody
    public ResultBean deleteRecord(@RequestBody Map<String, Integer> params) {
//        int count = recordMapper.deleteRecord(params.get("id"));
//        return ResultUtil.getResultBean(count, "删除失败");
        if (recordService.deleteRecord(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getRecord")
    @ResponseBody
    public ResultBean getRecord() {
        List<RecordBean> recordBeans = recordService.getRecord();
        return ResultUtil.getSuccessBean(recordBeans);
    }

    @RequestMapping("/getRecordByCondition")
    @ResponseBody
    public ResultBean getRecordByCondition(@RequestParam("condition") String condition) {
        List<RecordBean> recordBeans = recordService.getRecordByCondition(condition);
        return ResultUtil.getSuccessBean(recordBeans);
    }


}
