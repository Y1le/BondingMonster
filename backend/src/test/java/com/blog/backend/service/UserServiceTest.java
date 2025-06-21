package com.blog.backend.service;

import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import com.blog.backend.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension; // 确保导入
import org.springframework.security.crypto.password.PasswordEncoder; // 如果 AuthControllerTest 依赖它，这里也需要

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 启用 Mockito 扩展
@DisplayName("UserService 单元测试")
class UserServiceTest {

    @Mock // 模拟 UserMapper
    private UserMapper userMapper;

    @Mock // 模拟 RedisUtil
    private RedisUtil redisUtil;

    // @Mock // 如果 UserService 依赖 PasswordEncoder，则也需要模拟
    // private PasswordEncoder passwordEncoder;

    @InjectMocks // 注入模拟对象到 UserService 实例中
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encoded_password_hash"); // 模拟加密后的密码
        testUser.setPhoto("http://example.com/photo.jpg");
        testUser.setFollowerCount(100);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("获取用户信息 - 缓存命中")
    void getUserInfo_cacheHit() {
        // 模拟 redisUtil.get() 返回用户对象
        when(redisUtil.get(eq("user:info:1"))).thenReturn(testUser);

        User resultUser = userService.getUserInfo(1L);

        assertNotNull(resultUser);
        assertEquals(testUser.getId(), resultUser.getId());
        assertEquals(testUser.getUsername(), resultUser.getUsername());
        assertNull(resultUser.getPassword()); // 验证密码被清除

        // 验证 userMapper.selectById 没有被调用，因为缓存命中了
        verify(userMapper, never()).selectById(anyLong());
        verify(redisUtil, times(1)).get(eq("user:info:1"));
    }

    @Test
    @DisplayName("获取用户信息 - 缓存未命中，数据库查询成功")
    void getUserInfo_cacheMissAndDbSuccess() {
        // 模拟 redisUtil.get() 返回 null (缓存未命中)
        when(redisUtil.get(eq("user:info:1"))).thenReturn(null);
        // 模拟 userMapper.selectById() 返回用户对象
        when(userMapper.selectById(1L)).thenReturn(testUser);
        // 模拟 redisUtil.setEx() (void 方法)
        doNothing().when(redisUtil).setEx(eq("user:info:1"), any(User.class), anyLong(), eq(TimeUnit.HOURS));

        User resultUser = userService.getUserInfo(1L);

        assertNotNull(resultUser);
        assertEquals(testUser.getId(), resultUser.getId());
        assertEquals(testUser.getUsername(), resultUser.getUsername());
        assertNull(resultUser.getPassword()); // 验证密码被清除

        // 验证 userMapper.selectById 被调用
        verify(userMapper, times(1)).selectById(1L);
        // 验证 redisUtil.setEx 被调用 (将数据存入缓存)
        verify(redisUtil, times(1)).setEx(eq("user:info:1"), any(User.class), eq(1L), eq(TimeUnit.HOURS));
    }

    @Test
    @DisplayName("获取用户信息 - 缓存未命中，数据库查询失败 (用户不存在)")
    void getUserInfo_cacheMissAndDbFail() {
        // 模拟 redisUtil.get() 返回 null
        when(redisUtil.get(eq("user:info:999"))).thenReturn(null);
        // 模拟 userMapper.selectById() 返回 null
        when(userMapper.selectById(999L)).thenReturn(null);

        User resultUser = userService.getUserInfo(999L);

        assertNull(resultUser);

        verify(userMapper, times(1)).selectById(999L);
        verify(redisUtil, never()).setEx(anyString(), any(User.class), anyLong(), any(TimeUnit.class)); // 验证没有存入缓存
    }

    @Test
    @DisplayName("获取热门用户榜单 - 成功")
    void getTopUsers_success() {
        User user1 = new User(); user1.setId(1L); user1.setUsername("user1"); user1.setFollowerCount(200);
        User user2 = new User(); user2.setId(2L); user2.setUsername("user2"); user2.setFollowerCount(150);

        // 模拟 userMapper.selectTopUsers() 返回用户列表
        when(userMapper.selectTopUsers(5)).thenReturn(Arrays.asList(user1, user2));

        List<User> topUsers = userService.getTopUsers(5);

        assertNotNull(topUsers);
        assertEquals(2, topUsers.size());
        assertEquals("user1", topUsers.get(0).getUsername());
        assertEquals("user2", topUsers.get(1).getUsername());
        assertNull(topUsers.get(0).getPassword()); // 验证密码被清除

        verify(userMapper, times(1)).selectTopUsers(5);
    }

    @Test
    @DisplayName("更新粉丝数 - 成功")
    void updateFollowerCount_success() {
        Long userId = 1L;
        Integer count = 5;

        // 模拟 userMapper.updateFollowerCount() 返回 1 (表示更新成功)
        when(userMapper.updateFollowerCount(userId, count)).thenReturn(1);
        // 模拟 redisUtil.del() (void 方法)
        when(redisUtil.del(anyString())).thenReturn(true);
        // 模拟 redisUtil.zIncrementScore()
        when(redisUtil.zIncrementScore(eq("user:ranking"), eq(userId.toString()), eq(count.doubleValue()))).thenReturn(105.0); // 模拟更新后的分数

        userService.updateFollowerCount(userId, count);

        // 验证 userMapper.updateFollowerCount 被调用
        verify(userMapper, times(1)).updateFollowerCount(userId, count);
        // 验证 redisUtil.del 被调用 (清除用户缓存)
        verify(redisUtil, times(1)).del(eq("user:info:1"));
        // 验证 redisUtil.zIncrementScore 被调用 (更新排行榜)
        verify(redisUtil, times(1)).zIncrementScore(eq("user:ranking"), eq(userId.toString()), eq(count.doubleValue()));
    }
}