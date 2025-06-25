package com.blog.backend.service.Impl.user.account;

import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import com.blog.backend.service.Impl.utils.UserDetailsImpl;
import com.blog.backend.service.user.account.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService { // 实现 UserService 接口

    private final UserMapper userMapper;


    public UserServiceImpl(UserMapper userMapper /*, RedisUtil redisUtil*/) {
        this.userMapper = userMapper;
        // this.redisUtil = redisUtil;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    @Override // 标记为实现接口方法
    public ResponseEntity<Map<String, String>> getUserInfo(Long userId) {
        Map<String, String> map = new HashMap<>();
        if (userId == 0) {
            UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            User user = loginUser.getUser();
            map.put("id", user.getId().toString());
            map.put("username", user.getUsername());
            map.put("photo", user.getPhoto());
            map.put("followerCount",user.getFollowerCount().toString());
            return ResponseEntity.ok(map);
        }
        User user = userMapper.selectById(userId);

        // 检查用户是否存在，避免空指针
        if (user != null) {
            map.put("id", user.getId().toString());
            map.put("username", user.getUsername());
            map.put("photo", user.getPhoto());
            map.put("followerCount",user.getFollowerCount().toString());
            return ResponseEntity.ok(map);
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not found");
        System.out.println(errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 获取粉丝数排名前N的用户
     * @param limit 限制数量
     * @return 用户列表
     */
    @Override // 标记为实现接口方法
    public List<User> getTopUsers(int limit) {
        // 实际生产中，这里可以从 Redis 的 ZSet 中获取用户ID，然后批量查询用户信息
        // 为了简化，这里直接从数据库查询
        List<User> topUsers = userMapper.selectTopUsers(limit);
        // 清除敏感信息
        if (topUsers != null) {
            topUsers.forEach(user -> user.setPassword(null));
        }
        return topUsers;
    }

    /**
     * 更新用户的粉丝数
     * @param userId 用户ID
     * @param count 要更新的粉丝数
     */
    @Override // 标记为实现接口方法
    public void updateFollowerCount(Long userId, Integer count) {
        // 可以在这里添加一些业务逻辑，例如验证 userId 是否存在，或者 count 是否合法
        userMapper.updateFollowerCount(userId, count);
    }
}