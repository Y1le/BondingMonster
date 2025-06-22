package com.blog.backend.service;

import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import com.blog.backend.service.Impl.user.account.AuthServiceImpl;
import com.blog.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 启用 Mockito 扩展
@DisplayName("AuthService 单元测试")
class AuthServiceImplTest {

    @Mock // 模拟 UserMapper
    private UserMapper userMapper;

    @Mock // 模拟 PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @Mock // 模拟 JwtUtil
    private JwtUtil jwtUtil;

    @Mock // 模拟 RedisUtil
    private RedisUtil redisUtil;

    @InjectMocks // 注入模拟对象到 AuthService 实例中
    private AuthServiceImpl authServiceImpl;

    private User testUser;
    private final String rawPassword = "password123";
    private final String encodedPassword = "$2a$10$abcdefghijklmnopqrstuvw"; // 模拟加密后的密码
    private final String mockAccessToken = "mock.access.token";
    private final String mockRefreshToken = "mock.refresh.token";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword(encodedPassword);
        testUser.setPhoto("http://example.com/photo.jpg");
        testUser.setFollowerCount(10);
    }

    @Test
    @DisplayName("用户登录 - 成功")
    void login_success() {
        // 模拟 userMapper 的行为
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);
        // 模拟 passwordEncoder 的行为
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        // 模拟 redisUtil 的行为 (void 方法，不需要返回值，但可以验证是否被调用)
        doNothing().when(redisUtil).setEx(anyString(), anyString(), anyLong(), eq(TimeUnit.MILLISECONDS));

        Map<String, String> tokens = authServiceImpl.login("testuser", rawPassword);

        assertNotNull(tokens);
        assertEquals(mockAccessToken, tokens.get("access"));
        assertEquals(mockRefreshToken, tokens.get("refresh"));

        // 验证 mock 对象的方法是否被正确调用
        verify(userMapper, times(1)).selectByUsername("testuser");
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(redisUtil, times(1)).setEx(startsWith("jwt:refresh:1:"), eq(mockRefreshToken), eq(3600000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("用户登录 - 用户名不存在")
    void login_userNotFound() {
        when(userMapper.selectByUsername("nonexistent")).thenReturn(null);

        Map<String, String> tokens = authServiceImpl.login("nonexistent", rawPassword);

        assertNull(tokens);
        verify(userMapper, times(1)).selectByUsername("nonexistent");
        verifyNoInteractions(passwordEncoder, jwtUtil, redisUtil); // 验证其他依赖没有被调用
    }

    @Test
    @DisplayName("用户登录 - 密码错误")
    void login_wrongPassword() {
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Map<String, String> tokens = authServiceImpl.login("testuser", "wrongpassword");

        assertNull(tokens);
        verify(userMapper, times(1)).selectByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("wrongpassword", encodedPassword);
        verifyNoInteractions(jwtUtil, redisUtil);
    }

    @Test
    @DisplayName("刷新 Access Token - 成功")
    void refreshAccessToken_success() {
        when(userMapper.selectById(testUser.getId())).thenReturn(testUser);

        String newAccessToken = authServiceImpl.refreshAccessToken(mockRefreshToken);

        assertNotNull(newAccessToken);
        assertEquals(mockAccessToken, newAccessToken);
        verify(userMapper, times(1)).selectById(testUser.getId());
    }

    @Test
    @DisplayName("刷新 Access Token - Refresh Token 无效")
    void refreshAccessToken_invalidToken() {

        String newAccessToken = authServiceImpl.refreshAccessToken("invalid.refresh.token");

        assertNull(newAccessToken);
        verifyNoInteractions(userMapper); // 验证 userMapper 没有被调用
    }

    @Test
    @DisplayName("刷新 Access Token - 用户不存在")
    void refreshAccessToken_userNotFound() {
        when(userMapper.selectById(999L)).thenReturn(null);

        String newAccessToken = authServiceImpl.refreshAccessToken(mockRefreshToken);

        assertNull(newAccessToken);
        verify(userMapper, times(1)).selectById(999L);
    }
}