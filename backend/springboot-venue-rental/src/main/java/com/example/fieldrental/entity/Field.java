package com.example.fieldrental.entity;


import com.example.fieldrental.dto.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@lombok.Data
@Builder
@AllArgsConstructor
@Document(collection = "field")
@RequiredArgsConstructor
public class Field {
    @Id
    private String id ;
    private String name ;
    private String description ;
    private String address ;
    private LocalDateTime startTime ;
    private LocalDateTime endTime ;
    private String[] imageId ;
    private boolean status ; // 是否可以租借状态
    private double price ;
    private String positionId ;
}
