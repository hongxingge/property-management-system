package com.example.manage.controller;

import com.example.manage.bean.PayCostBean;
import com.example.manage.bean.PaymentOrderBean;
import com.example.manage.bean.ResultBean;
import com.example.manage.config.RequireRole;
import com.example.manage.mapper.PaymentOrderMapper;
import com.example.manage.service.AlipayService;
import com.example.manage.service.ParkingChargeService;
import com.example.manage.service.PayCostService;
import com.example.manage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payCost")
public class PayCostController {

    @Resource
    PayCostService payCostService;

    @Resource
    ParkingChargeService parkingChargeService;

    @Resource
    AlipayService alipayService;

    @Resource
    PaymentOrderMapper paymentOrderMapper;

    @Value("${alipay.front-url}")
    private String frontUrl;

    @RequestMapping("/updateParkingCharge")
    @ResponseBody
    public ResultBean updateParkingCharge(@RequestBody Map<String, Float> params) {
        if (parkingChargeService.updateParkingCharge(params.get("parkingCharge"))){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/getParkingCharge")
    @ResponseBody
    public ResultBean getParkingCharge() {
        float parkingCharge = parkingChargeService.getParkingCharge();
        return ResultUtil.getSuccessBean(parkingCharge, "获取成功");
    }

    /**
     * 下单:业主点"支付" → 查账单 → 鉴权(防越权付别人账单) → 生成订单 → 调起支付宝收银台
     * 返回 {orderNo, form},前端把 form 写入页面即可跳转到支付宝。
     */
    @RequestMapping("/createOrder")
    @RequireRole("owner")
    @ResponseBody
    public ResultBean createOrder(@RequestBody Map<String, Integer> params, HttpServletRequest request) {
        Integer costId = params.get("id");
        // 1. 查账单
        PayCostBean cost = payCostService.getCostById(costId);
        if (cost == null) {
            return ResultUtil.getFailBean("账单不存在");
        }
        if (cost.getPayState() == 2) {
            return ResultUtil.getFailBean("该账单已支付,无需重复支付");
        }
        // 2. 鉴权:这账单必须是当前业主的(JwtInterceptor 已把 uid 塞进 request 属性)
        String uid = (String) request.getAttribute("uid");
        if (!uid.equals(cost.getUid())) {
            return ResultUtil.getFailBean("无权支付他人账单");
        }
        // 3. 生成订单号 + 组装订单
        long now = System.currentTimeMillis();
        String orderNo = "PM" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                + (int) (Math.random() * 9000 + 1000);
        PaymentOrderBean order = new PaymentOrderBean();
        order.setOrderNo(orderNo);
        order.setCostId(cost.getId());
        order.setUid(uid);
        // float 转 BigDecimal 保留两位,金额用字符串传给支付宝(如 "300.00")
        order.setAmount(new BigDecimal(String.format("%.2f", cost.getPayMoney())));
        order.setPayChannel("alipay");
        order.setStatus(0);              // 待支付
        order.setCreateTime(now);
        order.setExpireTime(now + 15 * 60 * 1000L);  // 15 分钟超时
        paymentOrderMapper.insertOrder(order);       // useGeneratedKeys 会回填 order.id
        // 4. 调起支付宝收银台
        String formHtml = alipayService.tradePagePay(order);
        // 5. 返回给前端:订单号 + 支付表单 HTML
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("form", formHtml);
        return ResultUtil.getSuccessBean(data);
    }

    /**
     * 支付宝异步回调(生产环境主用)。
     * 支付宝服务器 POST 过来,不带 token(WebConfig 已放行)。
     * 必须返回纯文本 "success",否则支付宝会重试 8 次。
     */
    @RequestMapping("/notify")
    @ResponseBody
    public String notifyCallback(HttpServletRequest request) {
        // 1. 收集支付宝传来的全部参数
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }
        // 2. 验签:确认是支付宝发的,不是别人伪造的
        if (!alipayService.verifyNotify(params)) {
            return "fail";
        }
        // 3. 只处理支付成功的通知
        String tradeStatus = params.get("trade_status");
        if (!"TRADE_SUCCESS".equals(tradeStatus) && !"TRADE_FINISHED".equals(tradeStatus)) {
            return "success";
        }
        String orderNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");
        // 4. 幂等:SQL 层 WHERE status=0,已支付则影响 0 行,直接放行
        int rows = paymentOrderMapper.updateToPaid(orderNo, tradeNo, System.currentTimeMillis());
        if (rows == 0) {
            return "success";
        }
        // 5. 同步改账单 cost 为已支付(复用现有 pay 方法 = UPDATE cost SET pay_state=2)
        PaymentOrderBean order = paymentOrderMapper.getByOrderNo(orderNo);
        if (order != null) {
            payCostService.pay(order.getCostId());
        }
        return "success";
    }

    /**
     * 支付宝同步返回(浏览器跳回)。不加 @ResponseBody,返回 redirect 触发重定向。
     * 不信任同步参数(可伪造),用 out_trade_no 主动反查支付宝确认。
     */
    @RequestMapping("/return")
    public void returnCallback(@RequestParam("out_trade_no") String orderNo,
                                 HttpServletResponse response) throws IOException {
        if ("PAID".equals(alipayService.queryStatus(orderNo))) {
            int rows = paymentOrderMapper.updateToPaid(orderNo, null, System.currentTimeMillis());
            if (rows > 0) {
                PaymentOrderBean order = paymentOrderMapper.getByOrderNo(orderNo);
                if (order != null) {
                    payCostService.pay(order.getCostId());
                }
            }
        }
        // 重定向前端支付页,带订单号;前端轮询确认最终结果(paid=false 不代表失败,可能支付宝还在处理)
        //return "redirect:" + frontUrl + "/#/pay_cost?orderNo=" + orderNo + "&paid=" + paid;
        // orderNo 已在 createOrder 时由前端存进 sessionStorage,这里只重定向到费用页
        // 用 sendRedirect 而非 "redirect:",避免 Spring 对 # 做编码导致 hash 丢失
        response.sendRedirect(frontUrl + "/#/pay_cost");
    }

    /**
     * 查询订单状态(前端轮询用)。
     * 若订单仍待支付,顺手反查支付宝兜底(notify/return 可能延迟)。
     */
    @RequestMapping("/queryOrder")
    @RequireRole("owner")
    @ResponseBody
    public ResultBean queryOrder(@RequestParam("orderNo") String orderNo) {
        PaymentOrderBean order = paymentOrderMapper.getByOrderNo(orderNo);
        if (order == null) {
            return ResultUtil.getFailBean("订单不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("status", order.getStatus());
        if (order.getStatus() == 0) {
            String st = alipayService.queryStatus(orderNo);
            if ("PAID".equals(st)) {
                paymentOrderMapper.updateToPaid(orderNo, null, System.currentTimeMillis());
                payCostService.pay(order.getCostId());
                data.put("status", 1);
            } else if ("NOT_EXIST".equals(st)) {
                // 用户没付款就后退了,支付宝无此交易 → 告诉前端别再轮询
                data.put("stop", true);
                data.put("msg", "支付未完成,可重新支付");
            }
            // PENDING:还在待付款,前端继续轮询
        }
        return ResultUtil.getSuccessBean(data);
    }

    @RequestMapping("/pay")
    @RequireRole("owner")
    @ResponseBody
    public ResultBean pay(@RequestBody Map<String, Integer> params) {
        if (payCostService.pay(params.get("id"))){
            return ResultUtil.getResultBean(1, "支付成功");
        }
        return ResultUtil.getResultBean(0, "支付失败");
    }

    @RequestMapping("/addCost")
    @ResponseBody
    public ResultBean addCost(@Valid @RequestBody PayCostBean payCostBean) {
        payCostBean.setTime(System.currentTimeMillis());
        payCostBean.setPayState(1);
        payCostBean.setIsCheck(1);
        if (payCostService.addCost(payCostBean)){
            return ResultUtil.getResultBean(1, "添加成功");
        }
        return ResultUtil.getResultBean(0, "添加失败");
    }

    @RequestMapping("/updateCost")
    @ResponseBody
    public ResultBean updateCost(@Valid @RequestBody PayCostBean payCostBean) {
        if (payCostService.updateCost(payCostBean)){
            return ResultUtil.getResultBean(1, "修改成功");
        }
        return ResultUtil.getResultBean(0, "修改失败");
    }

    @RequestMapping("/deleteCost")
    @ResponseBody
    public ResultBean deleteCost(@RequestBody Map<String, Integer> params) {
        if (payCostService.deleteCost(params.get("id"))){
            return ResultUtil.getResultBean(1, "删除成功");
        }
        return ResultUtil.getResultBean(0, "删除失败");
    }

    @RequestMapping("/getCost")
    @ResponseBody
    public ResultBean getCost() {
        List<PayCostBean> payCostBeans = payCostService.getCost();
        return ResultUtil.getSuccessBean(payCostBeans);
    }

    @RequestMapping("/getCostByUid")
    @ResponseBody
    public ResultBean getCostByUid(@RequestParam("uid") String uid) {
        List<PayCostBean> payCostBeans = payCostService.getCostByUid(uid);
        return ResultUtil.getSuccessBean(payCostBeans);
    }
}
