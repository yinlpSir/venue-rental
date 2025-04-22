package com.example.fieldrental.handler;

import com.example.fieldrental.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentBuildUtil {

    @Autowired
    private RedisService redisService ;

    public String buildContent(String to,String control)
    {
        String code = getRandomCode();
        redisService.save(code.toLowerCase() , to , 180);    // redis 存储 code ： Email
        String contents = "";
        return contents ;
    }

    public String getRandomCode()
    {
        UuidUtil uuidUtil = new UuidUtil();
        String s = null;

        do{
            s = uuidUtil.get().substring(2,8);
        }while (redisService.get(s) == null) ;

        return s ;
    }
}
