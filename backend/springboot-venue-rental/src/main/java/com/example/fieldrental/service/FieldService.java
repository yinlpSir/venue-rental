package com.example.fieldrental.service;

import com.example.fieldrental.dto.BaiduMapDto;
import com.example.fieldrental.dto.FieldDto;
import com.example.fieldrental.dto.PageDto;
import com.example.fieldrental.entity.BaiduMap;
import com.example.fieldrental.entity.Field;

import java.util.List;

public interface FieldService {
    Field createField(FieldDto fieldDto) ;

    BaiduMap updateBaiduMap(BaiduMapDto baiduMapDto);

    List<BaiduMap> getAllBaiduMap();

    List<Field> getField(String search, PageDto pageDto);

    Field getFieldById(String fieldId) ;

    Field changeField(FieldDto fieldDto ) ;

    List<Field> findAll(PageDto pageDto);
    Field findField(String id);

    List<Field> getFieldByAddress(String address, PageDto pageDto);

    List<Field> getFieldByName(String name, PageDto pageDto);

    Field updateFiledStatusBySystem(Field field , Boolean status);
}
