package com.example.fieldrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//与阿里交互的对象
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AliPay {
    private String traceNo;// 我们的订单号
    private Double totalAmount;// 总金额
    private String subject;//主题
    private String alipayTraceNo;//阿里的流水号
    private String fieldId; // 用于支付成功后返回的订单详情
}
