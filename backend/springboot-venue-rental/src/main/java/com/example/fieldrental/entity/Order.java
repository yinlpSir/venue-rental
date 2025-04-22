package com.example.fieldrental.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String fieldId; // field id
    private double price; // 单价
    private int total; // 总价
    private LocalDateTime startTime; // 订单开始时间
    private LocalDateTime endTime; // 订单结束时间
    private boolean payStatus;// 支付状态
    private String addressIP ; //
    private long userId; // 用户Id
    private String coachId; //
    private boolean deviceRental; //
    // 如果存在就会唤醒第二角色教练
    // 直接存储 mongo redis 寄存 超时未支付 会一直处于未支付状态 如果 支付成功 会进入到支付成功状态
}
