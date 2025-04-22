package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.dto.JwtRequest;
import com.example.fieldrental.dto.JwtResponse;
import com.example.fieldrental.dto.RegisterRequest;
import com.example.fieldrental.entity.User;
import com.example.fieldrental.repository.UserRepository;
import com.example.fieldrental.service.UserService;
import com.example.fieldrental.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private JwtService jwtService;

    @Override
    public JwtResponse register(RegisterRequest request) {
        if(userRepository.getUserByUsername(request.getUsername()) == null) {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole("USER");
            user.setEmail(request.getEmail());

            // 注册时间即为当前时间
            user.setRegisterDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            User save = userRepository.save(user);
            if (save != null) {
                return getAuthResponse(user);
            }
            throw  new IllegalStateException("register failed");
        }
        else throw  new IllegalStateException("User already registered");
    }  // 注册 后面加个 邮箱认证 就行

    @Override
    public JwtResponse login(JwtRequest request) {
        User user = userRepository.getUserByUsername(request.getUsername());
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())&&(user.getRole().equals("USER")||user.getRole().equals("ADMIN")))
        {
            return  getAuthResponse(user);
        }
        return null;
    }

    private JwtResponse getAuthResponse(User user) {
        return new JwtResponse(jwtService.generateToken(user));
    }
}
