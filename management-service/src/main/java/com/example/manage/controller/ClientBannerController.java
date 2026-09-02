package com.example.manage.controller;

import com.example.manage.bean.ClientBannerBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.config.RequireRole;
import com.example.manage.mapper.ClientBannerMapper;
import com.example.manage.service.ClientBannerService;
import com.example.manage.utils.ResultUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clientBanner")
public class ClientBannerController {

    @Resource
    ClientBannerService bannerService;

    @RequestMapping("/addBanner")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean addBanner(@Valid @RequestBody ClientBannerBean bannerBean) {
        if (bannerService.addBanner(bannerBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateBanner")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean updateBanner(@Valid@RequestBody ClientBannerBean bannerBean) {
        if (bannerService.updateBanner(bannerBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteBanner")
    @RequireRole("admin")
    @ResponseBody
    public ResultBean deleteBanner(@Valid@RequestBody Map<String, Long> params) {
        if (bannerService.deleteBanner(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getBanner")
    @ResponseBody
    public ResultBean getBanner() {
        List<ClientBannerBean> devices = bannerService.getBanner();
        return ResultUtil.getSuccessBean(devices);
    }
}
