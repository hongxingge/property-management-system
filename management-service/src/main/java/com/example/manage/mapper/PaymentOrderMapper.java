package com.example.manage.mapper;

import com.example.manage.bean.PaymentOrderBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2026/9/3 19:31
 */
@Mapper
public interface PaymentOrderMapper {
    // 创建订单
    int insertOrder(PaymentOrderBean order);

    // 按订单号查(回调验签后用它定位订单)
    PaymentOrderBean getByOrderNo(@Param("orderNo") String orderNo);

    // 按账单id查最新一条订单(前端展示"最近一次支付"用)
    PaymentOrderBean getLatestByCostId(@Param("costId") Integer costId);

    // ★回调时改成已支付: 记录支付宝交易号+支付时间
    //   WHERE status=0 是 SQL 层幂等的关键——见下面 XML 注释
    int updateToPaid(@Param("orderNo") String orderNo,
                     @Param("tradeNo") String tradeNo,
                     @Param("payTime") Long payTime);

    // 超时关闭
    int updateToClosed(@Param("orderNo") String orderNo);

    List<PaymentOrderBean> listByUid(@Param("uid") String uid);
}