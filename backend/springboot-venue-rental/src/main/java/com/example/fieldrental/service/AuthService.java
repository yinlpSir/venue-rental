package com.example.fieldrental.service;

import com.example.fieldrental.dto.JwtRequest;
import com.example.fieldrental.dto.JwtResponse;
import com.example.fieldrental.dto.RegisterRequest;

public interface AuthService {
    JwtResponse register(RegisterRequest request);

    JwtResponse login(JwtRequest request);
}
