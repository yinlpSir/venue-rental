package com.example.fieldrental.controller;

import com.example.fieldrental.dto.OrderDto;
import com.example.fieldrental.entity.Order;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.handler.IPAddressGetterUtil;
import com.example.fieldrental.service.OrderService;
import com.example.fieldrental.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
@ResponseBody
@Tag(name = "订单查询")
public class OrderController {

    @Autowired
    private OrderService orderService ;

    @Autowired
    private RedisService redisService;

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public String createOrder(HttpServletRequest request , @RequestBody OrderDto orderDto) throws Exception {
        String ipAddress = IPAddressGetterUtil.getIpAddress(request);
        orderDto.setAddressIP(ipAddress);
        return orderService.createOrder(orderDto);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/check/{id}")
    @Operation(summary = "检查订单")
    public boolean check(@PathVariable String id)
    {
        return orderService.checkOrder(id);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/update")
    @Operation(summary = "更新订单")
    public boolean updateOrder(HttpServletRequest request , @RequestBody OrderDto orderDto) throws Exception
    {
        orderDto.setAddressIP(IPAddressGetterUtil.getIpAddress(request));
        return  orderService.updateOrder(orderDto);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/cancel/{id}")
    public void cancelOrder(@PathVariable String id) throws Exception{
        orderService.cancelOrder(id);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getOrder/{id}")
    @Operation(summary = "获取订单")
    public Order getOrder(@PathVariable String id) throws Exception
    {
        Order order = orderService.getOrder(id);
        return order;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getNoPay/{id}")
    @Operation(summary = "获取待付款订单")
    public List<Order> getNoPay(@PathVariable String id) throws Exception
    {
        List<Order> out = new ArrayList<>();
        List<Order> orders = orderService.getOrderListByUserId(id);
        orders.forEach(order -> {
            if (!order.isPayStatus()&&redisService.get(order.getId())!=null)
            {
                out.add(order);
            }
        });
        return out;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/byUserId/{id}")
    @Operation(summary = "获取用户订单")
    public List<Order> getOrderByUserId(@PathVariable String id) throws Exception{
        List<Order> orderListByUserId = orderService.getOrderListByUserId(id);
        return  orderListByUserId ;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/byField/{id}")
    @Operation(summary = "获取场地订单")
    public List<Order> getOrderByFieldId(@PathVariable String id) throws Exception{
        List<Order> orderListByFieldId = orderService.getOrderListByCoachId(id);
        return  orderListByFieldId ;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/byCoach/{id}")
    @Operation(summary = "获取教练订单")
    public List<Order> getOrderByCoachId(@PathVariable String id) throws Exception{
        List<Order> orderListByCoachId = orderService.getOrderListByCoachId(id);
        return  orderListByCoachId ;
    }

}
