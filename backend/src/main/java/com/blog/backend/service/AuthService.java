package com.blog.backend.service;


import com.blog.backend.entity.User;
import com.blog.backend.util.JwtUtil;
import com.blog.backend.util.RedisUtil;
import com.blog.backend.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 包含 access 和 refresh token 的 Map，或 null (认证失败)
     */
    public Map<String, String> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null; // 用户名或密码错误
        }

        // 生成 Access Token
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());

        // 生成 Refresh Token
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        // 将 Refresh Token 存储到 Redis，并设置过期时间
        // 键可以设计为 "jwt:refresh:<user_id>:<jti>"，jti 是 JWT ID，确保唯一性
        String refreshTokenKey = "jwt:refresh:" + user.getId() + ":" + UUID.randomUUID().toString(); // 简单的唯一键
        redisUtil.setEx(refreshTokenKey, refreshToken, jwtUtil.getRefreshTokenExpiration(), TimeUnit.MILLISECONDS);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", accessToken);
        tokens.put("refresh", refreshToken);
        return tokens;
    }

    /**
     * 刷新 Access Token
     * @param refreshToken 刷新令牌
     * @return 新的 Access Token，或 null (刷新失败)
     */
    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);

            // 检查用户是否存在
            User user = userMapper.selectById(userId);
            if (user != null) {
                return jwtUtil.generateAccessToken(user.getId(), user.getUsername());
            }
        }
        return null;
    }
}