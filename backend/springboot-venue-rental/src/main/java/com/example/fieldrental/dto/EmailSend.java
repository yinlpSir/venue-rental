package com.example.fieldrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSend {
    private String content ;
    private String toAddress ;
    private String from ;
    private String subject ;
}
