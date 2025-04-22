package com.example.fieldrental.repository;

import com.example.fieldrental.entity.BaiduMap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BaiduMapRepository extends MongoRepository<BaiduMap,String> {

    BaiduMap getBaiduMapByFieldId (String fieldId);

}
