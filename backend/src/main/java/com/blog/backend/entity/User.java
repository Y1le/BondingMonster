package com.blog.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Lombok 注解，自动生成 getter, setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password; // 存储加密后的密码
    private String photo;
    private Integer followerCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // JWT 认证时可能需要用到 Spring Security 的 UserDetails 接口，
    // 但这里为了简化，先不实现，只作为数据传输对象。
    // 如果要集成 Spring Security 的 UserDetailsService，User 类通常会实现 UserDetails 接口。
}