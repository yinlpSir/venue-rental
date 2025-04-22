package com.example.fieldrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Table
@Entity
@RequiredArgsConstructor
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id ;
    private String content ;
    private int rate ;
    private String fieldId ;
    private long userId ;
    private LocalDateTime sendTime ;
}
