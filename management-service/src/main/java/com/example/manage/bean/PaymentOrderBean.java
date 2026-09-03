package com.example.manage.bean;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @version 1.0
 * @description: 支付订单：一次真实的支付尝试。
 *               与 cost 账单分离——账单描述"欠多少",订单描述"这次支付"。
 * @date 2026/9/3 19:28
 */

public class PaymentOrderBean {
    private Integer id;
    private String orderNo;      // 商户订单号(传给支付宝的 out_trade_no)
    private Integer costId;      // 关联账单 cost.id
    private String uid;          // 业主 id
    private BigDecimal amount;   // 金额(元)
    private String payChannel;   // 支付渠道: alipay
    private Integer status;      // 0待支付 1已支付 2已关闭 3已退款
    private String tradeNo;      // 支付宝交易号
    private Long createTime;
    private Long payTime;
    private Long expireTime;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getCostId() { return costId; }
    public void setCostId(Integer costId) { this.costId = costId; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPayChannel() { return payChannel; }
    public void setPayChannel(String payChannel) { this.payChannel = payChannel; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getTradeNo() { return tradeNo; }
    public void setTradeNo(String tradeNo) { this.tradeNo = tradeNo; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getPayTime() { return payTime; }
    public void setPayTime(Long payTime) { this.payTime = payTime; }
    public Long getExpireTime() { return expireTime; }
    public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
}