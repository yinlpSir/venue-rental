package com.example.fieldrental.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "Collect")
@RequiredArgsConstructor
public class Collect {
    @Id
    private String id ;
    private List<String> collection ;
    private long userId ;
}
