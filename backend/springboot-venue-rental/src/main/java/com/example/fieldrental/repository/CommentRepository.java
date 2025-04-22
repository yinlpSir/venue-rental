package com.example.fieldrental.repository;

import com.example.fieldrental.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByFieldId (String fieldId);

    List<Comment> findCommentsByUserId (long UserId);

}
