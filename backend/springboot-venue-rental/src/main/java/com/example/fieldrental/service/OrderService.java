package com.example.fieldrental.service;

import com.example.fieldrental.dto.OrderDto;
import com.example.fieldrental.entity.Order;

import java.util.List;

public interface OrderService {
    String createOrder(OrderDto orderDto);

    Boolean checkOrder(String id) ;

    boolean updateOrder(OrderDto orderDto);

    void cancelOrder(String id);

    Order getOrder(String id) ;

    List<Order> getOrderListByUserId(String userId) ;

    List<Order> getOrderListByFieldId(String fieldId) ;

    List<Order> getOrderListByCoachId(String coachId) ;
}
