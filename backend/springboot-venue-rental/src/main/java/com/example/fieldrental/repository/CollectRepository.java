package com.example.fieldrental.repository;

import com.example.fieldrental.entity.Collect;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectRepository extends MongoRepository<Collect,String> {

    Collect getCollectByUserId(long userId);
}
