package com.blog.backend.service;

import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import com.blog.backend.util.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final RedisUtil redisUtil;

    public UserService(UserMapper userMapper, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserInfo(Long userId) {
        // 尝试从 Redis 缓存获取
        String cacheKey = "user:info:" + userId;
        User user = (User) redisUtil.get(cacheKey);

        if (user == null) {
            // 缓存中没有，从数据库查询
            user = userMapper.selectById(userId);
            if (user != null) {
                redisUtil.setEx(cacheKey, user, 1, TimeUnit.HOURS);
            }
        }

        if (user != null) {
            user.setPassword(null); // 清除密码信息
        }
        return user;
    }

    /**
     * 获取粉丝数排名前N的用户
     * @param limit 限制数量
     * @return 用户列表
     */
    public List<User> getTopUsers(int limit) {
        // 实际生产中，这里可以从 Redis 的 ZSet 中获取用户ID，然后批量查询用户信息
        // 为了简化，这里直接从数据库查询
        List<User> topUsers = userMapper.selectTopUsers(limit);
        // 清除敏感信息
        topUsers.forEach(user -> user.setPassword(null));
        return topUsers;
    }

    /**
     * 更新用户粉丝数 (通常由 RabbitMQ 消费者调用)
     * @param userId 用户ID
     * @param count 增加或减少的数量
     */
    public void updateFollowerCount(Long userId, Integer count) {
        userMapper.updateFollowerCount(userId, count);
        redisUtil.del("user:info:" + userId);
        redisUtil.zIncrementScore("user:ranking", userId.toString(), count); // member 可以是用户ID
    }
}