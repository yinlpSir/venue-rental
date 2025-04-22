package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(String key, Object value, int expirationTime) {

            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key , expirationTime ,TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o== null) return null;
        return String.valueOf(o);
    }

    @Override
    public void delete(String key) {
        if (redisTemplate.hasKey(key))
        redisTemplate.delete(key);
    }

    @Override
    public void update(String key, Object value, int expirationTime) {
        redisTemplate.opsForValue().set(key, value, expirationTime);
    }
}
