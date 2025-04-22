package com.example.fieldrental.service;

import com.example.fieldrental.dto.CommentDto;
import com.example.fieldrental.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentsByFliedId(String fliedId);

    List<Comment> getCommentsByUserId(long userId);

    Comment getComment(long id);

    Comment createComment(CommentDto commentDto) ;

    Comment updateComment(long id , CommentDto commentDto) ;

    boolean deleteComment(long id) ;
}
