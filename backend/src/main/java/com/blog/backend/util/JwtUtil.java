package com.blog.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {
    public static final long JWT_TTL = 5 * 60 * 1000L ;  // 5分钟
    public static final long RJWT_TTL = 60 * 60 * 1000L * 24 * 14 ;  // 14天
    public static final String JWT_KEY = "JSDFSDFSDFASJDHASDASDdfa32dJHASFDA67765asda123";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    public static String createRJWT(String subject) {
        // 刷新令牌的ID也可以用于在数据库中进行存储和验证，以实现吊销功能
        String uuid = getUUID();
        JwtBuilder builder = getJwtBuilder(subject, RJWT_TTL, uuid);
        return builder.compact();

    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }

        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("sg")
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 从刷新令牌中获取用户主题 (Subject)
     * @param refreshToken 刷新令牌
     * @return 用户主题 (通常是用户ID)
     * @throws Exception 如果令牌无效或解析失败
     */
    public static String getSubjectFromRefreshToken(String refreshToken) throws Exception {
        Claims claims = parseJWT(refreshToken);
        return claims.getSubject();
    }

    /**
     * 刷新访问令牌
     * @param refreshToken 客户端提供的刷新令牌
     * @return 新的访问令牌
     * @throws Exception 如果刷新令牌无效或过期
     */
    public static String refreshAccessToken(String refreshToken) throws Exception {

        Claims claims = parseJWT(refreshToken);

        String subject = claims.getSubject();

        return createJWT(subject);
    }

    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
