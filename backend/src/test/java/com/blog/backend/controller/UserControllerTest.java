package com.blog.backend.controller;

import com.blog.backend.entity.User;
import com.blog.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) // 针对 UserController 进行 Web 层测试 [1, 2]
@DisplayName("UserController 应该")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // 用于模拟 HTTP 请求 [1, 2]

    @MockBean // Spring Boot Test 提供的注解，用于创建 Mockito 模拟并注入到 Spring 上下文 [1, 2]
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper; // 用于将 Java 对象转换为 JSON 字符串 [3]

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        // 初始化测试用户数据，注意这里密码为 null，因为 getUserInfo 会清除密码
        testUser1 = new User(1L, "john.doe", null, "photo1.jpg", 100, null, null);
        testUser2 = new User(2L, "jane.doe", null, "photo2.jpg", 150, null, null);
    }

    @Nested
    @DisplayName("处理获取用户信息请求时 (GET /myspace/getinfo)")
    class GetUserInfoTests {

        @Test
        @DisplayName("返回现有用户信息和 200 OK")
        void shouldReturnUserInfoWhenUserExists() throws Exception {
            // Given
            when(userService.getUserInfo(1L)).thenReturn(testUser1);

            // When & Then
            mockMvc.perform(get("/myspace/getinfo")
                            .param("user_id", "1") // 设置查询参数 [4]
                            .accept(MediaType.APPLICATION_JSON)) // 期望接收 JSON [4]
                    .andExpect(status().isOk()) // 期望 HTTP 状态码为 200 OK [4]
                    .andExpect(jsonPath("$.id").value(testUser1.getId())) // 验证 JSON 响应中的字段 [4]
                    .andExpect(jsonPath("$.username").value(testUser1.getUsername()))
                    .andExpect(jsonPath("$.photo").value(testUser1.getPhoto()))
                    .andExpect(jsonPath("$.followerCount").value(testUser1.getFollowerCount()))
                    .andExpect(jsonPath("$.password").doesNotExist()); // 密码不应该返回 [4]

            verify(userService, times(1)).getUserInfo(1L); // 验证 userService 方法被调用
        }

        @Test
        @DisplayName("用户不存在时返回 404 Not Found 和错误信息")
        void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
            // Given
            when(userService.getUserInfo(anyLong())).thenReturn(null);

            // When & Then
            mockMvc.perform(get("/myspace/getinfo")
                            .param("user_id", "999")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound()) // 期望 HTTP 状态码为 404 Not Found
                    .andExpect(jsonPath("$.error").value("User not found")); // 验证错误信息

            verify(userService, times(1)).getUserInfo(999L);
        }

        @Test
        @DisplayName("缺少 user_id 参数时返回 400 Bad Request")
        void shouldReturnBadRequestWhenUserIdParamIsMissing() throws Exception {
            // When & Then
            mockMvc.perform(get("/myspace/getinfo")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()); // 期望 400 Bad Request

            verify(userService, never()).getUserInfo(anyLong()); // 验证 userService 未被调用
        }
    }

    @Nested
    @DisplayName("处理获取用户榜单请求时 (GET /api/user/topUsers)")
    class GetTopUsersTests {

        @Test
        @DisplayName("返回热门用户列表和 200 OK")
        void shouldReturnTopUsers() throws Exception {
            // Given
            List<User> topUsers = Arrays.asList(testUser1, testUser2);
            // 注意：userService.getTopUsers(5) 是硬编码在 UserController 中的
            when(userService.getTopUsers(5)).thenReturn(topUsers);

            // When & Then
            mockMvc.perform(get("/api/user/topUsers")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success")) // 验证状态字段
                    .andExpect(jsonPath("$.data.length()").value(2)) // 验证数据列表长度 [4]
                    .andExpect(jsonPath("$.data[0].username").value(testUser1.getUsername()))
                    .andExpect(jsonPath("$.data[0].password").doesNotExist()) // 密码不应该返回
                    .andExpect(jsonPath("$.data[1].username").value(testUser2.getUsername()))
                    .andExpect(jsonPath("$.data[1].password").doesNotExist()); // 密码不应该返回

            verify(userService, times(1)).getTopUsers(5);
        }

        @Test
        @DisplayName("没有热门用户时返回空列表和 200 OK")
        void shouldReturnEmptyListWhenNoTopUsers() throws Exception {
            // Given
            when(userService.getTopUsers(anyInt())).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/user/topUsers")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.length()").value(0)); // 验证数据列表为空

            verify(userService, times(1)).getTopUsers(5);
        }
    }
}