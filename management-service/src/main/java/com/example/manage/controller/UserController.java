package com.example.manage.controller;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.bean.UserBean;
import com.example.manage.config.RequireRole;
import com.example.manage.service.TokenBlacklistService;
import com.example.manage.service.UserService;
import com.example.manage.utils.JwtUtil;
import com.example.manage.utils.MD5Util;
import com.example.manage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    TokenBlacklistService tokenBlacklistService;

    @Resource
    UserService userService;

    @RequestMapping("/addUser")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean addUser(@Valid @RequestBody UserBean userBean) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        userBean.setUid(uuid);
        //userBean.setPwd(MD5Util.MD5(userBean.getPwd()));
        userBean.setPwd(BCrypt.hashpw(userBean.getPwd(), BCrypt.gensalt()));
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
    @RequireRole("admin")
    @ResponseBody
    public ResultBean updateUserByUid(@Valid @RequestBody UserBean userBean, HttpServletRequest request) {
        userBean.setPwd(BCrypt.hashpw(userBean.getPwd(), BCrypt.gensalt()));
        if (userService.updateUserByUid(userBean)){
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                tokenBlacklistService.blacklist(authHeader.substring(7));
            }
            return ResultUtil.getResultBean(1, "修改成功,请重新登录");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteUserByUid")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean deleteUserByUid(@RequestBody Map<String, String> params) {
        if (userService.deleteUserByUid(params.get("uid"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/queryUser")
    @RequireRole("admin")
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
        if (BCrypt.checkpw(userBean.getPwd(), user.getPwd())) {
            user.setPwd("");
            Map<String, Object> data = new HashMap<>();
            data.put("token", JwtUtil.generateToken(user.getUid(), user.getName(), "admin"));
            data.put("user", user);
            return ResultUtil.getSuccessBean(data);
        } else {
            return ResultUtil.getFailBean("密码不正确");
        }

    }

    @RequestMapping("/queryUserByPhone")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean queryUserByPhone() {
        UserBean user = userService.queryUserByPhone("");
        return ResultUtil.getSuccessBean(user);
    }

}
