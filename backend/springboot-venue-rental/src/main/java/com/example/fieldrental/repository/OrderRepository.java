package com.example.fieldrental.repository;

import com.example.fieldrental.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    Order getOrderByAddressIP(String IP);

    List<Order> getOrdersByUserId(long userId);

    List<Order> getOrdersByCoachId(long coachId);

    List<Order> getOrdersByFieldId(String fieldId);

}
