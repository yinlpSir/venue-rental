package com.example.fieldrental.controller;

import com.example.fieldrental.dto.CoachDto;
import com.example.fieldrental.entity.Coach;
import com.example.fieldrental.entity.User;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.service.CoachService;
import com.example.fieldrental.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/coach")
@ResponseBody
@Tag(name="教练用户")
public class CoachController {
    @Autowired
    private CoachService coachService;

    @Autowired
    private UserService userService;

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getCoachStats/{id}")
    @Operation(summary  = "获取状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",//参数名字
                    value = "教练id",//参数的描述
                    required = true,//是否必须传入
                    //paramType定义参数传递类型：有path,query,body,form,header
                    paramType = "path"
            )
    })
    public boolean getStats(@PathVariable long id) throws IOException {
       return coachService.getById(id).isStatus();
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getCoachById/{id}")
    @Operation(summary  = "获取教练信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",//参数名字
                    value = "教练id",//参数的描述
                    required = true,//是否必须传入
                    //paramType定义参数传递类型：有path,query,body,form,header
                    paramType = "path"
            )
    })
    public Coach getCoach(@PathVariable long id) throws IOException {
        return coachService.getById(id);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @GetMapping("/getCoachByUserId/{id}")
    @Operation(summary  = "通过用户id获取教练信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",//参数名字
                    value = "用户id",//参数的描述
                    required = true,//是否必须传入
                    //paramType定义参数传递类型：有path,query,body,form,header
                    paramType = "path"
            )
    })
    public Coach getCoachByUserId(@PathVariable long id) throws IOException {
        return coachService.getByUserId(id);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/register")
    @Operation(summary  = "注册教练",description = "{    long userId;\n" +
            "    String description ;\n" +
            "    String type ;\n" +
            "    double price ;}")
    public Coach register(@RequestBody CoachDto coachDto) throws IOException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return coachService.createCoach(coachDto);
    }
}
