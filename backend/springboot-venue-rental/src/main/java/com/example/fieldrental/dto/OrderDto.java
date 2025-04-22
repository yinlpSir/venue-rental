package com.example.fieldrental.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {
    private String id ;
    private String fieldId;
    private double price;
    private int total;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean payStatus;
    private String addressIP ;
    private long userId;
    private String coachId;
    private boolean deviceRental;
}
