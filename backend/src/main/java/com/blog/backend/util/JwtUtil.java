package com.blog.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration; // 毫秒

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration; // 毫秒

    private Key key; // 用于签名和验证 JWT 的密钥

    /**
     * 在 bean 初始化后执行，用于生成密钥
     */
    @PostConstruct
    public void init() {
        // 使用 Keys.hmacShaKeyFor 从 secret 字符串生成 HMAC-SHA 密钥
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成 Access Token
     * @param userId 用户ID
     * @param username 用户名
     * @return Access Token 字符串
     */
    public String generateAccessToken(Long userId, String username) {
        return generateToken(userId, username, accessTokenExpiration);
    }

    /**
     * 生成 Refresh Token
     * @param userId 用户ID
     * @param username 用户名 (通常 Refresh Token 中不需要用户名，但为了通用性可以保留)
     * @return Refresh Token 字符串
     */
    public String generateRefreshToken(Long userId, String username) {
        return generateToken(userId, username, refreshTokenExpiration);
    }

    /**
     * 通用方法：生成 JWT Token
     * @param userId 用户ID
     * @param username 用户名
     * @param expiration 有效期 (毫秒)
     * @return JWT Token 字符串
     */
    private String generateToken(Long userId, String username, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username); // 将用户名也放入 claims

        return Jwts.builder()
                .setClaims(claims) // 设置自定义 claims
                .setIssuedAt(new Date(System.currentTimeMillis())) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256) // 使用密钥和算法签名
                .compact(); // 压缩成 JWT 字符串
    }

    /**
     * 从 Token 中提取所有 Claims
     * @param token JWT Token 字符串
     * @return Claims 对象
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key) // 设置签名密钥
                .build()
                .parseClaimsJws(token) // 解析 JWT
                .getBody(); // 获取 Claims 部分
    }

    /**
     * 从 Token 中提取特定 Claim
     * @param token JWT Token 字符串
     * @param claimsResolver 用于从 Claims 中提取特定值的函数
     * @param <T> 期望的返回值类型
     * @return 提取到的值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从 Token 中获取用户ID
     * @param token JWT Token 字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        // 使用 extractClaim 方法，传入一个 lambda 表达式来获取 userId
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * 从 Token 中获取用户名
     * @param token JWT Token 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        // 使用 extractClaim 方法，传入一个 lambda 表达式来获取 username
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    /**
     * 从 Token 中获取过期时间
     * @param token JWT Token 字符串
     * @return 过期时间 Date 对象
     */
    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 判断 Token 是否过期
     * @param token JWT Token 字符串
     * @return 如果 Token 已过期则返回 true，否则返回 false
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 验证 Access Token 是否有效且未过期
     * @param token Access Token 字符串
     * @return 如果 Token 有效且未过期则返回 true，否则返回 false
     */
    public Boolean validateAccessToken(String token) {
        try {
            // 尝试解析 Token，如果解析失败（如签名无效、Token格式错误），会抛出异常
            extractAllClaims(token);
            // 如果解析成功，再检查是否过期
            return !isTokenExpired(token);
        } catch (Exception e) {
            // Token 解析失败或签名无效，视为无效 Token
            // logger.warn("Access Token validation failed: " + e.getMessage()); // 可以在这里添加日志
            return false;
        }
    }

    /**
     * 验证 Refresh Token 是否有效且未过期
     * @param token Refresh Token 字符串
     * @return 如果 Token 有效且未过期则返回 true，否则返回 false
     */
    public Boolean validateRefreshToken(String token) {
        try {
            // 尝试解析 Token
            extractAllClaims(token);
            // 检查是否过期
            return !isTokenExpired(token);
        } catch (Exception e) {
            // Refresh Token 解析失败或签名无效
            // logger.warn("Refresh Token validation failed: " + e.getMessage()); // 可以在这里添加日志
            return false;
        }
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}