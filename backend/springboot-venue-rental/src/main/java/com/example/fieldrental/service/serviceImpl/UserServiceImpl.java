package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.entity.User;
import com.example.fieldrental.repository.UserRepository;
import com.example.fieldrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
   private UserRepository userRepository ;

    @Override
    public User getUserById(long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) { throw  new IllegalArgumentException("User not found") ;}
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        return user;
    }

    @Override
    public Boolean uploadImg(String username, String img) {
        User user = userRepository.getUserByUsername(username);
        user.setImg(img);
        return userRepository.save(user) == null? false : true;
    }
}
