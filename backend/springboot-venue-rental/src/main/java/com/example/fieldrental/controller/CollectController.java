package com.example.fieldrental.controller;


import com.example.fieldrental.entity.Collect;
import com.example.fieldrental.entity.Comment;
import com.example.fieldrental.entity.Field;
import com.example.fieldrental.entity.User;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.repository.CollectRepository;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.service.FieldService;
import com.example.fieldrental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/collect")
@ResponseBody
@Tag(name = "收藏管理")
public class CollectController {

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private UserService userService;


    @Autowired
    private FieldService fieldService;

//    @HasAnyRole(roles = {"USER","ADMIN"})
//    @Operation(summary = "获取")
//    @GetMapping("/{id}")
//    public Collect get(@PathVariable long id )
//    {
//        Collect collect = collectRepository.getCollectByUserId(id);
//        if (!Objects.nonNull(collect))
//            collect = collectRepository.save(new Collect(null,new ArrayList<>(),id));
//        List<String> collection = collect.getCollection();
//        for (String a : collection)
//        {
//            if (Objects.isNull(fieldService.getFieldById(a).getId()))
//            {
//                collection.remove(a);
//            }
//        }
//        return collectRepository.save(collect);
//    }
    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "获取")
    @GetMapping("/{id}")
    public Collect get(@PathVariable long id )
    {
        Collect collect = collectRepository.getCollectByUserId(id);
        if (!Objects.nonNull(collect))
            collect = collectRepository.save(new Collect(null,new ArrayList<>(),id));
        List<String> collection = collect.getCollection();
        for (int i = 0; i < collection.size(); i++)
        {
            Field field = fieldService.findField(collection.get(i));
            if (field.getId()==null||field.getId().equals("") || field==null)
            {
                collection.remove(i);
            }
        }
        collectRepository.save(collect);
        return collect;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "取消收藏")
    @PostMapping("/del/{userId}/{fieldId}")
    public Collect del(@PathVariable long userId,@PathVariable String fieldId )
    {
        Collect collect = collectRepository.getCollectByUserId(userId);

        List<String> fieldIds = collect.getCollection();
        boolean removeStatus = fieldIds.remove(fieldId);
        if(removeStatus){
            // remove successfully
            collect.setCollection(fieldIds);
        }
        return collectRepository.save(collect);
    }


    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "获取列表")
    @GetMapping("/get/{id}")
    public List<String> getList (@PathVariable long id )
    {
        Collect collect = collectRepository.getCollectByUserId(id);
        return collect.getCollection();
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "检查是否收藏")
    @PostMapping("/check/{id}")
    public boolean check(@PathVariable String id )
    {
        User userByUsername = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Collect collectByUserId = collectRepository.getCollectByUserId(userByUsername.getId());
        return collectByUserId.getCollection().contains(id);
    }


    /***
     *   会删除重复的收藏 直接在前端对collect进行处理 然后统一提交就行
     *
     * ****/
    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "更新")
    @PostMapping("/update")
    public boolean update(@RequestBody Collect collect){
        if (Objects.nonNull(collect))
        {
            Map<String,Integer> set = new HashMap<>();

            collect.getCollection().forEach(e -> set.put(e,1));
            List<String> lists = new ArrayList<>();
            set.entrySet().forEach(key -> lists.add(String.valueOf(key)));  // 消除重复的list 非常好用
            collect.setCollection(lists);
            collectRepository.save(collect);
            return true ;
        }
        else throw new NullPointerException("collect has null field value");
    }

/**
 * 添加场地id 到用户收藏夹。 注意我没有添加 securityContextHolder.getContext().isAuthoration() 去确定用户id
 * @param id 用户id
 * @param id2 场地id
 * @return ture 收藏成功 false 重复收藏或者该场地失效
 *
 * **/
    @Autowired
    private FieldRepository fieldRepository ;
    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "添加")
    @PostMapping("/add/{id}/{id2}")
    public boolean add(@PathVariable long id, @PathVariable String id2){
        Collect collect = collectRepository.getCollectByUserId(id);
        if (Objects.nonNull(collect) && fieldRepository.findById(id2)!=null)
        {
            int i = collect.getCollection().indexOf(id2);
            if (i == -1)
                collect.getCollection().add(id2);
            else return false;
            collectRepository.save(collect);
            return true ;
        }
        else throw new NullPointerException("collect has null field value");
    }
}
