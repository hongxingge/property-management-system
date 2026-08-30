package com.example.manage.controller;

import com.example.manage.bean.CarportBean;
import com.example.manage.bean.ClientNewsBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.mapper.ClientBannerMapper;
import com.example.manage.mapper.ClientNewsMapper;
import com.example.manage.service.ClientNewsService;
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
@RequestMapping("/clientNews")
public class ClientNewsController {

    @Resource
    ClientNewsService clientNewsService;

    @RequestMapping("/addNews")
    @ResponseBody
    public ResultBean addNews(@RequestBody ClientNewsBean newsBean) {
        newsBean.setTime(System.currentTimeMillis());
        ClientNewsBean bean = clientNewsService.getNewsById((long) newsBean.getId());
        if (bean != null) {
            return   ResultUtil.getFailBean("该编号已存在，请修改后提交");
        }
        if (clientNewsService.addNews(newsBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateNews")
    @ResponseBody
    public ResultBean updateNews(@RequestBody ClientNewsBean newsBean) {
        newsBean.setTime(System.currentTimeMillis());
        if (clientNewsService.updateNews(newsBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }


    @RequestMapping("/deleteNews")
    @ResponseBody
    public ResultBean deleteNews(@RequestBody Map<String, Long> params) {
        if (clientNewsService.deleteNews(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }


    @RequestMapping("/getNews")
    @ResponseBody
    public ResultBean getNews() {
        List<ClientNewsBean> news = clientNewsService.getNews();
        return ResultUtil.getSuccessBean(news);
    }


    @RequestMapping("/getNewsById")
    @ResponseBody
    public ResultBean getNewsById(@RequestParam("id") Long id) {
        ClientNewsBean news = clientNewsService.getNewsById(id);
        return ResultUtil.getSuccessBean(news);
    }
}
