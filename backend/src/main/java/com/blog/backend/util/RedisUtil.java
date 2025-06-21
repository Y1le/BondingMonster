package com.blog.backend.util;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setEx(String key, Object value, long timeout, java.util.concurrent.TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    public Double zIncrementScore(String key, String member, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, member, score);
    }
}