package com.example.manage.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.example.manage.bean.PaymentOrderBean;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description: 支付宝业务封装。
 *              - tradePagePay:下单,返回自动提交的 HTML 表单,前端写入即跳转收银台。
 *              - tradeQuery:主动反查订单真实状态,同步返回时用它二次确认,防伪造。
 * @date 2026/9/3 19:46
 */
@Service
public class AlipayService {
    @Resource
    private AlipayClient alipayClient;

    @Value("${alipay.return-url}") private String returnUrl;
    @Value("${alipay.notify-url}") private String notifyUrl;
    @Value("${alipay.alipay-public-key}") private String alipayPublicKey;
    @Value("${alipay.charset}") private String charset;
    @Value("${alipay.sign-type}") private String signType;

    /**
     * 电脑网站支付(电脑网站支付 product_code = FAST_INSTANT_TRADE_PAY)
     */
    public String tradePagePay(PaymentOrderBean order) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(returnUrl);
        request.setNotifyUrl(notifyUrl);
        String bizContent = String.format(
                "{\"out_trade_no\":\"%s\",\"total_amount\":\"%s\",\"subject\":\"%s\",\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}",
                order.getOrderNo(),
                order.getAmount().toPlainString(),
                "物业账单支付"
        );
        request.setBizContent(bizContent);
        try {
            // pageExecute 返回一段 <form>...自动 submit 的 HTML,支付宝收银台地址已写死在里面
            return alipayClient.pageExecute(request).getBody();
        } catch (Exception e) {
            throw new RuntimeException("调起支付宝失败: " + e.getMessage(), e);
        }
    }

    /**
     * 反查支付宝订单真实状态。
     * @return "PAID" 已支付成功 | "NOT_EXIST" 支付宝无此交易(用户没付款就后退) | "PENDING" 待付款或查询失败
     */
    public String queryStatus(String orderNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{\"out_trade_no\":\"" + orderNo + "\"}");
        try {
            AlipayTradeQueryResponse resp = alipayClient.execute(request);
            // 打印支付宝真实返回,排查用
            System.out.println("【查单】" + orderNo + " code=" + resp.getCode()
                    + " trade_status=" + resp.getTradeStatus()
                    + " sub_code=" + resp.getSubCode()
                    + " msg=" + resp.getMsg());
            if ("10000".equals(resp.getCode())
                    && ("TRADE_SUCCESS".equals(resp.getTradeStatus())
                    || "TRADE_FINISHED".equals(resp.getTradeStatus()))) {
                return "PAID";
            }
            if ("ACQ.TRADE_NOT_EXIST".equals(resp.getSubCode())) {
                return "NOT_EXIST";
            }
            return "PENDING";
        } catch (Exception e) {
            e.printStackTrace();   // 把被吞掉的异常打出来
            return "PENDING";
        }
    }

    /**
     * 验签:校验支付宝回调请求的签名,确认是支付宝本人发的、没被篡改。
     * 这是防止"别人伪造一个回调说已支付"的关键。
     */
    public boolean verifyNotify(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, alipayPublicKey, charset, signType);
        } catch (AlipayApiException e) {
            return false;
        }
    }
}