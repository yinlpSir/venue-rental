package com.example.fieldrental.repository;

import com.example.fieldrental.dto.PageDto;
import com.example.fieldrental.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends MongoRepository<Field , String> {
    @Query(value = "{$or: [{'name': {$regex: ?0, $options: 'i'}}, {'id': {$regex: ?0, $options: 'i'}},{'description': {$regex: ?0, $options: 'i'}} ,{ 'address': {$regex: ?0, $options: 'i'}} ]} ")
    Page<Field> getFieldByNameAndAddressAndDescriptionAndId (String search, Pageable pageable);

    Page<Field> getFieldByAddress(String address , Pageable pageable);

    Page<Field> getFieldByName(String name , Pageable pageable);
}
