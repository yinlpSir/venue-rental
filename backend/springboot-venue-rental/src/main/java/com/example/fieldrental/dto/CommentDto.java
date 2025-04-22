package com.example.fieldrental.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String content ;
    private int rate ;
    private String fieldId ;
    private long userId ;
}
