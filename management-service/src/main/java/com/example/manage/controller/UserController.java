package com.example.manage.controller;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.bean.UserBean;
import com.example.manage.service.UserService;
import com.example.manage.utils.MD5Util;
import com.example.manage.utils.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/addUser")
    @ResponseBody
    public ResultBean addUser(@RequestBody UserBean userBean) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        userBean.setUid(uuid);
        userBean.setPwd(MD5Util.MD5(userBean.getPwd()));
        UserBean user = userService.queryUserByPhone(userBean.getPhone());
        if (user != null) {
            return   ResultUtil.getFailBean("该用户已存在，请修改后提交");
        }
        if (userService.addUser(userBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateUserByUid")
    @ResponseBody
    public ResultBean updateUserByUid(@RequestBody UserBean userBean) {
        userBean.setPwd(MD5Util.MD5(userBean.getPwd()));
        if (userService.updateUserByUid(userBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteUserByUid")
    @ResponseBody
    public ResultBean deleteUserByUid(@RequestBody Map<String, String> params) {
        if (userService.deleteUserByUid(params.get("uid"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/queryUser")
    @ResponseBody
    public ResultBean queryUser() {
        List<UserBean> userBeans = userService.queryUser();
        return ResultUtil.getSuccessBean(userBeans);
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultBean login(@RequestBody UserBean userBean) {
        UserBean user = userService.queryUserByPhone(userBean.getPhone());
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

    @RequestMapping("/queryUserByPhone")
    @ResponseBody
    public ResultBean queryUserByPhone() {
        UserBean user = userService.queryUserByPhone("");
        return ResultUtil.getSuccessBean(user);
    }

}
