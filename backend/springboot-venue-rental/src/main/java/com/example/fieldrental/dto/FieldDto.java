package com.example.fieldrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldDto {
    private String id ;
    private String name ;
    private String description ;
    private String address ;
    private LocalDateTime startTime ;
    private LocalDateTime endTime ;
    private String[] imageId ;
    private boolean status ;
    private double price ;
    private Position position;
}
