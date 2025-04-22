package com.example.fieldrental.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@lombok.Data
@Table
@Entity
@RequiredArgsConstructor
public class Coach {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private long userId;
    private String description ;
    private String type ;
    private boolean status ;
    private double price ;  // pre hour price
}
