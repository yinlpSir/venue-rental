package com.example.fieldrental.service;

import com.example.fieldrental.entity.User;

public interface UserService {
    User getUserById(long userId);
    User getUserByUsername(String username);

    Boolean uploadImg(String username , String img);
}
