package com.example.manage.controller;

import com.example.manage.bean.ResultBean;
import com.example.manage.bean.RoomUserBean;
import com.example.manage.bean.UserBean;
import com.example.manage.service.RoomUserService;
import com.example.manage.utils.MD5Util;
import com.example.manage.utils.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/roomUser")
public class RoomUserController {

    @Resource
    RoomUserService roomUserService;

    @RequestMapping("/addRoomUser")
    @ResponseBody
    public ResultBean addRoomUser(@RequestBody RoomUserBean roomUserBean) {
        RoomUserBean user = roomUserService.getRoomUserByPhone(roomUserBean.getPhone());
        if (user != null) {
            return ResultUtil.getFailBean("添加失败,手机号已存在");
        }
        String id = UUID.randomUUID().toString().replace("-", "");
        roomUserBean.setId(id);
        roomUserBean.setPwd(MD5Util.MD5("123456"));
        if (roomUserService.addRoomUser(roomUserBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultBean login(@RequestBody UserBean userBean) {
        RoomUserBean user = roomUserService.getRoomUserByPhone(userBean.getPhone());
        if (user == null) {
            return ResultUtil.getFailBean("无该用户");
        }
        if (user.getPwd().equals(userBean.getPwd())) {
            user.setPwd("");
            return ResultUtil.getSuccessBean(user);
        } else {
            return ResultUtil.getFailBean("密码不正确");
        }

    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ResultBean updatePwd(@RequestBody RoomUserBean roomUserBean) {
        RoomUserBean user = roomUserService.getRoomUserByPhone(roomUserBean.getPhone());
        if (user == null) {
            return ResultUtil.getFailBean("无该用户");
        }

        if (user.getPwd().equals(roomUserBean.getPwd())) {
            roomUserBean.setuPwd(MD5Util.MD5(roomUserBean.getuPwd()));
            if (roomUserService.updatePwd(roomUserBean)){
                return ResultUtil.getResultBean(1, "修改成功");
            }
            return ResultUtil.getResultBean(0, "修改失败");
        } else {
            return ResultUtil.getFailBean("原密码不正确");
        }
    }

    @RequestMapping("/deleteRoomUserById")
    @ResponseBody
    public ResultBean deleteRoomUserById(@RequestBody Map<String, String> params) {
        if (roomUserService.deleteRoomUserById(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/updateRoomUserById")
    @ResponseBody
    public ResultBean updateRoomUserById(@RequestBody RoomUserBean roomUserBean) {
        if (roomUserService.updateRoomUserById(roomUserBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/queryRoomUser")
    @ResponseBody
    public ResultBean queryRoomUser() {
        List<RoomUserBean> roomUserBeans = roomUserService.queryRoomUser();
        return ResultUtil.getSuccessBean(roomUserBeans);
    }

    @RequestMapping("/queryRoomUserByCondition")
    @ResponseBody
    public ResultBean queryRoomUserByCondition(@RequestParam("condition") String condition) {
        List<RoomUserBean> roomUserBeans = roomUserService.queryRoomUserByCondition(condition);
        return ResultUtil.getSuccessBean(roomUserBeans);
    }

}
