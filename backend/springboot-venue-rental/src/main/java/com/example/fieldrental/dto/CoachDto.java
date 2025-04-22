package com.example.fieldrental.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoachDto {
    private long userId;
    private String description ;
    private String type ;
    private boolean status ;
    private double price ;
}
