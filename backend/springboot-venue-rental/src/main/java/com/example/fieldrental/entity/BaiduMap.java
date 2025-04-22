package com.example.fieldrental.entity;

import com.example.fieldrental.dto.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "map_point")
public class BaiduMap {
    @Id
    private String id ;
    private Position position ;
    private String fieldId ;
    private String fieldName ;
    private String description;
    private LocalDateTime updateTime ;
    private long updateId ;
}
