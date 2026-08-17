package com.devch105.shortly.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private String getKey(String shortCode){
        return  "url:"+shortCode;
    }

    public void saveUrl(String shortCode , String url){
        redisTemplate.opsForValue()
                .set(getKey(shortCode), url, Duration.ofHours(1));
    }

    public String getUrl(String shortCode){
        return  redisTemplate.opsForValue().get(getKey(shortCode));
    }

    public void  deleteUrl(String shortCode){
        redisTemplate.delete(getKey(shortCode));
    }

}
