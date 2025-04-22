package com.example.fieldrental.controller;

import com.example.fieldrental.dto.BaiduMapDto;
import com.example.fieldrental.dto.FieldDto;
import com.example.fieldrental.dto.PageDto;
import com.example.fieldrental.entity.BaiduMap;
import com.example.fieldrental.entity.Field;
import com.example.fieldrental.entity.Order;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.repository.BaiduMapRepository;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.service.FieldService;
import com.example.fieldrental.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/field")
@Tag(name = "场地租借")
@ResponseBody
public class FieldController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private BaiduMapRepository baiduMapRepository;

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "删除场地")
    @PostMapping("/del/{id}")
    public boolean delete(@PathVariable String id )
    {
        Field field = fieldService.getFieldById(id);
        fieldRepository.delete(field);
        return true ;
    }


//    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "获取场地坐标")
    @PostMapping("/getPosition/{id}")
    public BaiduMap getPosition (@PathVariable String id)
    {
        BaiduMap baiduMap = baiduMapRepository.findById(id).orElse(null);
        if (baiduMap == null)
            baiduMap = baiduMapRepository.getBaiduMapByFieldId(id);
        return baiduMap;
    }

    @Operation(summary  = "场地不可租赁时间")
    @PostMapping("/date/{id}")
    @HasAnyRole(roles = {"ADMIN"})
    public Boolean[] getFieldsCanBeRental(@PathVariable String id) {
        Boolean[] result = new Boolean[31];
        for (int i = 0; i < result.length; i++) {
            result[i] = true;
        }
        List<Order> orders = orderService.getOrderListByFieldId(id);
        if (orders.size() > 0){
            orders.forEach(order -> {
                if (order.getStartTime().isAfter(LocalDateTime.now())||order.getEndTime().isAfter(LocalDateTime.now()))
                {
                    int dayOfYear = LocalDateTime.now().getDayOfYear();
                    int flag = order.getStartTime().getDayOfYear() - dayOfYear;
                    for (int i = flag > 0 ? flag : 0; i < order.getEndTime().getDayOfYear()-dayOfYear; i++)
                    {
                        result[i] = false;
                    }

                }
            });
        }
        return result;
    }


    @Operation(summary  = "场地创建")
    @PostMapping("/create")
    @HasAnyRole(roles = {"ADMIN"})
    public Field createField(@RequestBody FieldDto fieldDto)
    {
        Field field = fieldService.createField(fieldDto);
        if (field != null)
        return field;
        else throw new IllegalStateException("出现了异常");
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "模糊查询场地")
    @PostMapping("/search/{value}")
    public List<Field> searchFields(@RequestBody PageDto pageDto, @PathVariable String value)
    {
        List<Field> field = fieldService.getField(value, pageDto);
        return field;
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "通过id查询场地")
    @PostMapping("/get/{id}")
    public Field getFieldById(@PathVariable String id)
    {
        return fieldService.getFieldById(id);
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "更新场地坐标")
    @PostMapping("/changeMap")
    public Field updateFieldPoint( @RequestBody BaiduMapDto baiduMapDto)
    {
        fieldService.updateBaiduMap(baiduMapDto);
        return fieldService.getFieldById(baiduMapDto.getFieldId());
    }

    @Operation(summary = "获取分页")
    @GetMapping("/total")
    public int getTotal()
    {
        List<Field> all = fieldRepository.findAll();
        return all.size();
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "获取所有场地的坐标")
    @PostMapping("/getAllPosition")
    public List<BaiduMap> getAll ()
    {
        return fieldService.getAllBaiduMap();
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "更改场地信息")
    @PostMapping("/update")
    public Field update(@RequestBody FieldDto fieldDto)
    {
        Field field = fieldService.changeField(fieldDto);
        if (field != null)
        return field;
        else
            throw new IllegalStateException("Field " + fieldDto.getName() + "is can not be change");
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "获取全部")
    @PostMapping("/all")
    public List<Field> getAll(@RequestBody PageDto pageDto)
    {
        return fieldService.findAll(pageDto);
    }
    //    @HasAnyRole(roles = {"ADMIN","USER"})
//    @Operation(summary = "获取全部")
//    @PostMapping("/all")
//    public List<Field> getAllField(@RequestBody PageDto pageDto)
//    {
//        return fieldService.findAll(pageDto);
//    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "通过地址获取")
    @PostMapping("/getByAddress/{address}")
    public List<Field> getByAddress(@RequestBody PageDto pageDto,@PathVariable String address)
    {
        return fieldService.getFieldByAddress(address,pageDto);
    }

    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "通过场地名获取")
    @PostMapping("/getByName/{name}")
    public List<Field> getByName(@RequestBody PageDto pageDto,@PathVariable String name)
    {
        return fieldService.getFieldByAddress(name,pageDto);
    }

    /**
     * 这里面会涉及到更改field的status的代码
     * @param id
     * @return
     */
    @HasAnyRole(roles = {"ADMIN","USER"})
    @Operation(summary = "获取场地订单并判断是否可以租借")
    @PostMapping("/is/{id}")
    public boolean getFieldStatus(@PathVariable String id )
    {
        Field field = fieldService.getFieldById(id);
        if (Objects.nonNull(field))
        {
            List<Order> orderListByFieldId = orderService.getOrderListByFieldId(field.getId());
            for (Order order : orderListByFieldId)
            {
                if (order.isPayStatus()==false||order.getStartTime().isAfter(LocalDateTime.now())||order.getEndTime().isBefore(LocalDateTime.now()))
                    continue;
                else
                {
                    fieldService.updateFiledStatusBySystem(field,false);
                    return false;
                }
            }
            fieldService.updateFiledStatusBySystem(field,true);
        }
        return true ;
    }
}
