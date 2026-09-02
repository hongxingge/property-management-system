package com.example.manage.controller;

import com.example.manage.bean.ResultBean;
import com.example.manage.bean.RoomUserBean;
import com.example.manage.bean.UserBean;
import com.example.manage.config.RequireRole;
import com.example.manage.service.RoomUserService;
import com.example.manage.utils.JwtUtil;
import com.example.manage.utils.ResultUtil;
import jakarta.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/roomUser")
public class RoomUserController {

    @Resource
    RoomUserService roomUserService;

    @RequestMapping("/addRoomUser")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean addRoomUser(@RequestBody RoomUserBean roomUserBean) {
        RoomUserBean user = roomUserService.getRoomUserByPhone(roomUserBean.getPhone());
        if (user != null) {
            return ResultUtil.getFailBean("添加失败,手机号已存在");
        }
        String id = UUID.randomUUID().toString().replace("-", "");
        roomUserBean.setId(id);
        //roomUserBean.setPwd(MD5Util.MD5("123456"));
        roomUserBean.setPwd(BCrypt.hashpw("123456", BCrypt.gensalt()));
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
        if (BCrypt.checkpw(userBean.getPwd(), user.getPwd())) {
            user.setPwd("");
            Map<String, Object> data = new HashMap<>();
            data.put("token", JwtUtil.generateToken(user.getId(), user.getName(), "owner"));
            data.put("user", user);
            return ResultUtil.getSuccessBean(data);
        } else {
            return ResultUtil.getFailBean("密码不正确");
        }

    }

    @RequestMapping("/updatePwd")
    @RequireRole("owner")
    @ResponseBody
    public ResultBean updatePwd(@RequestBody RoomUserBean roomUserBean) {
        RoomUserBean user = roomUserService.getRoomUserByPhone(roomUserBean.getPhone());
        if (user == null) {
            return ResultUtil.getFailBean("无该用户");
        }

        if (BCrypt.checkpw(roomUserBean.getPwd(), user.getPwd())) {
            //roomUserBean.setuPwd(MD5Util.MD5(roomUserBean.getuPwd()));
            roomUserBean.setuPwd(BCrypt.hashpw(roomUserBean.getuPwd(), BCrypt.gensalt()));
            if (roomUserService.updatePwd(roomUserBean)){
                return ResultUtil.getResultBean(1, "修改成功");
            }
            return ResultUtil.getResultBean(0, "修改失败");
        } else {
            return ResultUtil.getFailBean("原密码不正确");
        }
    }

    @RequestMapping("/deleteRoomUserById")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean deleteRoomUserById(@RequestBody Map<String, String> params) {
        if (roomUserService.deleteRoomUserById(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/updateRoomUserById")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean updateRoomUserById(@RequestBody RoomUserBean roomUserBean) {
        if (roomUserService.updateRoomUserById(roomUserBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/queryRoomUser")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean queryRoomUser() {
        List<RoomUserBean> roomUserBeans = roomUserService.queryRoomUser();
        return ResultUtil.getSuccessBean(roomUserBeans);
    }

    @RequestMapping("/queryRoomUserByCondition")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean queryRoomUserByCondition(@RequestParam("condition") String condition) {
        List<RoomUserBean> roomUserBeans = roomUserService.queryRoomUserByCondition(condition);
        return ResultUtil.getSuccessBean(roomUserBeans);
    }

}
