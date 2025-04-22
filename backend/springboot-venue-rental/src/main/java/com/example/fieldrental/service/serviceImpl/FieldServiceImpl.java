package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.dto.BaiduMapDto;
import com.example.fieldrental.dto.FieldDto;
import com.example.fieldrental.dto.PageDto;
import com.example.fieldrental.entity.BaiduMap;
import com.example.fieldrental.entity.Field;
import com.example.fieldrental.repository.BaiduMapRepository;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.repository.UserRepository;
import com.example.fieldrental.service.FieldService;
import com.example.fieldrental.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("fieldService")
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private BaiduMapRepository baiduMapRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public Field createField(FieldDto fieldDto) {
//        Field field = new Field(null,fieldDto.getName(), fieldDto.getDescription(), fieldDto.getAddress(),
//                fieldDto.getStartTime(),fieldDto.getEndTime(),fieldDto.getImageId(),true,fieldDto.getPrice(),null);
//        Field save = fieldRepository.save(field);
//        BaiduMap baiduMap = new BaiduMap(null,fieldDto.getPosition(),field.getId(),LocalDateTime.now(),userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId() );
//        baiduMapRepository.save(baiduMap);
//        save.setPositionId(baiduMap.getId());
//        save = fieldRepository.save(field);
//        return save;
//    }

    @Override
    public Field createField(FieldDto fieldDto) {
        Field field = new Field(null,fieldDto.getName(), fieldDto.getDescription(), fieldDto.getAddress(),
                fieldDto.getStartTime(),fieldDto.getEndTime(),fieldDto.getImageId(),true,fieldDto.getPrice(),null);
        Field save = fieldRepository.save(field);
        BaiduMap baiduMap = new BaiduMap(null,fieldDto.getPosition(),field.getId(),fieldDto.getName(),fieldDto.getDescription(),LocalDateTime.now(),userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        baiduMapRepository.save(baiduMap);
        save.setPositionId(baiduMap.getId());
        save = fieldRepository.save(field);
        return save;
    }

    @Override
    public BaiduMap updateBaiduMap(BaiduMapDto baiduMapDto) {
        Field field = fieldRepository.findById(baiduMapDto.getId()).orElse(null);
        if (Objects.nonNull(field))
        {
            BaiduMap baiduMap = baiduMapRepository.getBaiduMapByFieldId(field.getId());
            if (baiduMap == null)
                baiduMap = new BaiduMap();
            baiduMap.setUpdateId(baiduMapDto.getUpdateId());
            baiduMap.setPosition(baiduMapDto.getPosition());
            baiduMap.setUpdateTime(LocalDateTime.now());

            baiduMap=baiduMapRepository.save(baiduMap);
            field.setPositionId(baiduMap.getId());
            fieldRepository.save(field);
            return baiduMap;
        }
        throw new IllegalArgumentException("this field doesn't found");
    }

    @Override
    public List<BaiduMap> getAllBaiduMap() {
        return baiduMapRepository.findAll();
    }

    @Override
    public List<Field> getField(String search, PageDto pageDto) {
        PageRequest request = getRequest(pageDto);
        List<Field> list = getList(fieldRepository.getFieldByNameAndAddressAndDescriptionAndId(search, request));
        return list;
    }

    @Override
    public Field getFieldById(String fieldId) {
//        return fieldRepository.findById(fieldId).orElse(null);
        return fieldRepository.findById(fieldId).orElse(new Field());
    }

    @Override
    public Field changeField(FieldDto fieldDto) {
        Field field = fieldRepository.findById(fieldDto.getId()).orElse(null);
        if (field != null) {
            return fieldRepository.save(new Field(fieldDto.getId(),
                    fieldDto.getName(),
                    fieldDto.getDescription(),
                    fieldDto.getAddress(),
                    fieldDto.getStartTime(),
                    fieldDto.getEndTime(),
                    fieldDto.getImageId(),
                    true,
                    fieldDto.getPrice(), field.getPositionId()));


        }
        return null;
    }

    @Override
    public List<Field> findAll(PageDto pageDto) {
        PageRequest request = getRequest(pageDto);
        return getList(fieldRepository.findAll(request));
    }

    @Override
    public Field findField(String id) {
        Field field = fieldRepository.findById(id).orElse(new Field());
        return field;
    }

    @Override
    public List<Field> getFieldByAddress(String address, PageDto pageDto) {
        PageRequest request = getRequest(pageDto);
        List<Field> list = getList(fieldRepository.getFieldByAddress(address, request));
        return list;
    }

    @Override
    public List<Field> getFieldByName(String name, PageDto pageDto) {
        PageRequest request = getRequest(pageDto);
        List<Field> list = getList(fieldRepository.getFieldByName(name, request));
        return list;
    }

    @Override
    public Field updateFiledStatusBySystem(Field field, Boolean status) {
        field.setStatus(status);
        redisService.delete(field.getId());
        return fieldRepository.save(field);
    }



    public PageRequest getRequest(PageDto pageDto)
    {
        return  PageRequest.of(pageDto.getPage(),pageDto.getSize(), Sort.by(Sort.Direction.DESC,"status"));
    }

    public List<Field> getList(Page<Field> entity)
    {
        List<Field> entitys = new ArrayList<>();
        long id = -1;
        for (Field entity1 : entity) {
            if (entity1.getName() != null && !entity1.getName().equals(""))
                entitys.add(entity1);}
        return entitys;
    }
}