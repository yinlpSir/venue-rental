package com.example.fieldrental.controller;


import com.example.fieldrental.dto.JwtRequest;
import com.example.fieldrental.dto.JwtResponse;
import com.example.fieldrental.dto.RegisterRequest;
import com.example.fieldrental.entity.Field;
import com.example.fieldrental.entity.User;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.repository.UserRepository;
import com.example.fieldrental.service.AuthService;
import com.example.fieldrental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@ResponseBody
public class IndexController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @HasAnyRole(roles = {"ADMIN"})
    @GetMapping("/getall")
    public List<User> getAll() {
        return  userRepository.findAll();
    }

    @HasAnyRole(roles = {"ADMIN"})
    @GetMapping("/ban/{id}")
    public boolean banned(@PathVariable long id){
        User user = userService.getUserById(id);
        if (user == null || user.getRole().equals("ADMIN"))
            return false;
        user.setRole("null");
        userRepository.save(user);
        return true;
    }

    @HasAnyRole(roles = {"ADMIN"})
    @GetMapping("/unban/{id}")
    public Boolean unbanned(@PathVariable long id)
    {
        User user = userService.getUserById(id);
        if (user == null || user.getRole().equals("ADMIN"))
            return false;
        user.setRole("USER");
        userRepository.save(user);
        return true;
    }

    @GetMapping("/u/{username}")
    public boolean changeUsername(@PathVariable String username)
    {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(name);
        if (user == null)
            throw new RuntimeException("用户不存在");
        else {
            user.setUsername(username);
            userRepository.save(user);
            SecurityContextHolder.clearContext();
        }
        return true;
    }

    @GetMapping("/e/{email}")
    public boolean changeEmail(@PathVariable String email)
    {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(name);
        if (user == null)
            throw new RuntimeException("用户不存在");
        else {
            user.setEmail(email);
            userRepository.save(user);
        }
        return true;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request){
        if (request != null) {
            JwtResponse login = authService.login(request);
            if (login != null) {
                return  login;
            }
            throw new RuntimeException("the password or username is error");
        }
        throw new IllegalArgumentException("haven't this user");
    }

    @GetMapping("/self")
    public User getUser ()
    {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) { throw new IllegalArgumentException("未查询到该user") ;}
        else return user;
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable long userId){
        User user = userRepository.getUserById(userId);
        return user;
    }

    @PostMapping("/changePassword/{newPassword}")
    @Operation(summary = "更改密码")
    public Boolean changePassword(@RequestBody JwtRequest request, @PathVariable String newPassword)
    {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getUsername().equals(request.getUsername()))
        {
            if (passwordEncoder.matches(request.getPassword(),user.getPassword()))
            {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
            throw new RuntimeException("check the password");
        }
        throw new RuntimeException("please enter a truth username");
    }

    @PostMapping("/register")
    public JwtResponse register(@RequestBody RegisterRequest request)
    {
        JwtResponse register = authService.register(request);
        if (register == null) {
            throw new RuntimeException("register failed");
        }
        return register ;
    }


    @GetMapping("/v3/causal")
    public String[] getCausal()
    {
        return new String[]{
                "https://img.zcool.cn/community/01b3275ec294fda8012148146566f4.jpg?x-oss-process=image/auto-orient,1/resize,m_lfit,w_1280,limit_1/sharpen,100/quality,q_100",
                "https://bpic.51yuansu.com/backgd/cover/00/00/09/5b45df082f32d.jpg?x-oss-process=image/resize,w_780/sharpen,100",
                "https://preview.qiantucdn.com/58pic/28/84/90/49558PICm0aefa99b03zd_PIC2018.jpg%21qt_gao320"
        };
//        return new String[] {
//                "https://shp.qpic.cn/ishow/2735093017/1664531256_1265602313_10387_sProdImgNo_2.jpg/0" ,
//                "https://shp.qpic.cn/ishow/2735030510/1709604284_829394697_41148_sProdImgNo_2.jpg/0" ,
//                "https://th.bing.com/th/id/R.9d950c382cbbb76693f78f4d2359c74b?rik=smLuNkuWzOMDzw&riu=http%3a%2f%2fi0.hdslb.com%2fbfs%2farchive%2fcc380b1b28282506d4fda7c1a2d36b77692c837c.jpg&ehk=dk3YrxV7j0aN1eueRTuupgDW5D44%2bco8kkq0GZlWSW0%3d&risl=&pid=ImgRaw&r=0"};
    }
}
