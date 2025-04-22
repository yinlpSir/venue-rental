package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.dto.OrderDto;
import com.example.fieldrental.entity.Order;
import com.example.fieldrental.repository.CoachRepository;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.repository.OrderRepository;
import com.example.fieldrental.service.OrderService;
import com.example.fieldrental.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedisService redisService ;
    @Autowired
    private OrderRepository orderRepository ;

    @Autowired
    private FieldRepository fieldRepository ;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CoachRepository coachRepository ;
    @Override
    public String createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setAddressIP(orderDto.getAddressIP());
        order.setDeviceRental(orderDto.isDeviceRental());
        order.setCoachId(orderDto.getCoachId());
        order.setPayStatus(false);
        order.setTotal(orderDto.getTotal());
        order.setStartTime(orderDto.getStartTime() == null ? LocalDateTime.now() : orderDto.getStartTime());
        order.setEndTime(orderDto.getEndTime());
        order.setUserId(orderDto.getUserId());
        order.setFieldId(orderDto.getFieldId());
        order.setPrice(orderDto.getPrice());
//        if (order.getCoachId() != null) {
//            order.setPrice
//                    (orderDto.getPrice()+
//                            (coachRepository.findById(Long.valueOf
//                                            (order.getCoachId()))
//                                    .orElse(null))
//                                    .getPrice()* order.getTotal()); }
//        if (order.getFieldId() != null) {
//            order.setPrice
//                    (orderDto.getPrice()+
//                            (fieldRepository.findById
//                                            (order.getFieldId())
//                                    .orElse(null))
//                                    .getPrice()* order.getTotal()); }

        Order save = orderRepository.save(order);
        redisService.save(save.getId(),save.getUserId() ,60*30);
        return save.getId();
    }

    @Override
    public Boolean checkOrder(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(Objects.nonNull(order))
        {
            if (order.isPayStatus())
                return true ;
            Object o = redisTemplate.opsForValue().get(id);
            if (Objects.nonNull(o))
                return false;
            return null;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean updateOrder(OrderDto orderDto) {

        Order order = orderRepository.findById(orderDto.getId()).orElse(null);
        order.setAddressIP(orderDto.getAddressIP());
        order.setDeviceRental(orderDto.isDeviceRental());
        order.setCoachId(orderDto.getCoachId());
        order.setEndTime(orderDto.getEndTime());
        order.setPayStatus(false);
        order.setTotal(orderDto.getTotal());
        order.setStartTime(orderDto.getStartTime());
        order.setUserId(orderDto.getUserId());
        order.setFieldId(orderDto.getFieldId());
        if (order.getCoachId() != null) {
            order.setPrice
                    (orderDto.getPrice()+
                            (coachRepository.findById(Long.valueOf
                                            (order.getCoachId()))
                                    .orElse(null))
                                    .getPrice()* order.getTotal()); }
        if (order.getFieldId() != null) {
            order.setPrice
                    (orderDto.getPrice()+
                            (fieldRepository.findById
                                            (order.getFieldId())
                                    .orElse(null))
                                    .getPrice()* order.getTotal()); }
        Order save = orderRepository.save(order);
        if (save != null)
            return true;
        else return false;
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && !order.getFieldId().equals(" "))
            redisService.delete(orderId);
        orderRepository.delete(order);
    }

    @Override
    public Order getOrder(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            throw new IllegalStateException("Order not found for id " + id);
        else
            return order;
    }

    @Override
    public List<Order> getOrderListByUserId(String userId) {
        List<Order> order = orderRepository.getOrdersByUserId(Long.parseLong(userId));
        if (order == null)
            throw new IllegalStateException("Order not found for userId " + userId);
        else
            return order;
    }

    @Override
    public List<Order> getOrderListByFieldId(String fieldId) {
        List<Order> order = orderRepository.getOrdersByFieldId(fieldId);
        if (order == null)
            throw new IllegalStateException("Order not found for userId " + fieldId);
        else
            return order;
    }

    @Override
    public List<Order> getOrderListByCoachId(String coachId) {
        List<Order> order = orderRepository.getOrdersByCoachId(Long.parseLong(coachId));
        if (order == null)
            throw new IllegalStateException("Order not found for userId " + coachId);
        else
            return order;
    }
}
