package com.example.fieldrental.repository;

import com.example.fieldrental.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<Image,String> {
    List<Image> getImageByUserId (long userId);
}
