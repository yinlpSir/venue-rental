package com.example.fieldrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiduMapDto {
    private String id ;
    private Position position ;
    private String fieldId ;
    private LocalDateTime updateTime ;
    private long updateId ;
}
