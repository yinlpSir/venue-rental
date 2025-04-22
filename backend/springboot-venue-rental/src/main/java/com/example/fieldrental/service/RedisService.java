package com.example.fieldrental.service;

public interface RedisService {
    void save(String key, Object value, int expirationTime);

    String get(String key);
    void delete(String key);

    void update(String key, Object value, int expirationTime);
}
