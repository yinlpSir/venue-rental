package com.example.fieldrental.controller;

import com.example.fieldrental.dto.CommentDto;
import com.example.fieldrental.entity.Comment;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
@ResponseBody
@Tag(name = "评论管理")
public class CommentController {

    @Autowired
    private CommentService commentService ;

    @Operation(summary = "获取评论fieldId")
    @GetMapping("/fieldId/{id}")
    public List<Comment> getByFieldId(@PathVariable String id ){
        List<Comment> commentsByFliedId = commentService.getCommentsByFliedId(id);
        return commentsByFliedId ;
    }

    @Operation(summary = "获取评论userId")
    @GetMapping("/userId/{id}")
    public List<Comment> getByUserId(@PathVariable long id ){
        List<Comment> comments = commentService.getCommentsByUserId(id);
        return comments ;
    }

    @Operation(summary = "获取评论")
    @GetMapping("/id/{id}")
    public Comment get(@PathVariable long id ){
        Comment comments = commentService.getComment(id);
        return comments ;
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "创建")
    @PostMapping("/create")
    public Comment create(@RequestBody CommentDto commentDto)
    {
        return commentService.createComment(commentDto);
    }


    /**
     * id 在URL 中携带 获取相对应的fiald 然后对其更改
     *
     * @param id
     * @param commentDto
     * 需要注意的是 commentDto中不携带id
     * ***/
    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "更新")
    @PostMapping("/update/{id}")
    public Comment update(@RequestBody CommentDto commentDto,@PathVariable long id)
    {
        return commentService.updateComment(id,commentDto);
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @Operation(summary = "删除")
    @PostMapping("/del/{id}")
    public boolean del(@PathVariable long id){
        return commentService.deleteComment(id);
    }

    // 我言人间春正好，君却话罢江南处。
    // 何不下扬州？正当瘦西湖。
    // 奈何风月一别春不在，徒留秋叶飘零孤提岸。
    // 应是马踏浅草灼烈酒，而君向东我向西。
}
