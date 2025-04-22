package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.dto.CommentDto;
import com.example.fieldrental.entity.Comment;
import com.example.fieldrental.repository.CommentRepository;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.repository.UserRepository;
import com.example.fieldrental.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository ;

    @Autowired
    private FieldRepository fieldRepository ;

    @Autowired
    private UserRepository userRepository ;

    @Override
    public List<Comment> getCommentsByFliedId(String fliedId) {
        if (fieldRepository.findById(fliedId).orElse(null)== null)
            throw new EntityNotFoundException("Couldn't find"+fliedId) ;

        List<Comment> comments = commentRepository.findCommentsByFieldId(fliedId);

        return comments;
    }

    @Override
    public List<Comment> getCommentsByUserId(long userId) {
        if (userRepository.findById(userId).orElse(null)== null)
            throw new EntityNotFoundException("Couldn't find"+userId) ;
        List<Comment> comments = commentRepository.findCommentsByUserId(userId);
        return comments;
    }

    @Override
    public Comment getComment(long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (Objects.nonNull(comment))
        return comment;
        else throw new EntityNotFoundException("Couldn't find"+id);
    }

    @Override
    public Comment createComment(CommentDto commentDto) {
        if (!Objects.nonNull(commentDto))
            throw new IllegalArgumentException("have null element check the field");
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setRate(commentDto.getRate());
        comment.setUserId(commentDto.getUserId());
        comment.setFieldId(commentDto.getFieldId());
        comment.setSendTime(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(long id ,CommentDto commentDto) {
        if (!Objects.nonNull(commentDto))
            throw new IllegalArgumentException("have null element check the field");
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            throw new EntityNotFoundException("not found"+id);
        comment.setContent(commentDto.getContent());
        comment.setRate(commentDto.getRate());
        comment.setUserId(commentDto.getUserId());
        comment.setFieldId(commentDto.getFieldId());
        comment.setSendTime(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public boolean deleteComment(long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            throw new EntityNotFoundException("not found"+id);
        commentRepository.delete(comment);
        return true;
    }
}
