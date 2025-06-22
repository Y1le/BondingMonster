package com.blog.backend.controller;

import com.blog.backend.controller.user.account.AuthController;
import com.blog.backend.service.Impl.user.account.AuthServiceImpl;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class) // 针对 AuthController 进行 Web 层测试 [1, 2]
@DisplayName("AuthController 应该")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // 用于模拟 HTTP 请求 [1, 2]

    @MockBean // Spring Boot Test 提供的注解，用于创建 Mockito 模拟并注入到 Spring 上下文 [1, 2]
    private AuthServiceImpl authServiceImpl;

    @Autowired
    private ObjectMapper objectMapper; // 用于将 Java 对象转换为 JSON 字符串 [3]

    @BeforeEach
    void setUp() {
        // 每个测试方法执行前可以进行一些通用设置，这里暂时不需要特别的初始化
    }

    @Nested
    @DisplayName("处理用户登录请求时 (POST /api/token)")
    class LoginTests {

        @Test
        @DisplayName("成功登录并返回 access 和 refresh token")
        void shouldReturnTokensOnSuccessfulLogin() throws Exception {
            // Given
            String username = "testuser";
            String password = "testpassword";
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access", "mocked_access_token");
            tokens.put("refresh", "mocked_refresh_token");

            when(authServiceImpl.login(username, password)).thenReturn(tokens); // 模拟登录成功

            // When & Then
            mockMvc.perform(post("/api/token")
                            .param("username", username) // 设置查询参数 [4]
                            .param("password", password) // 设置查询参数 [4]
                            .accept(MediaType.APPLICATION_JSON)) // 期望接收 JSON [4]
                    .andExpect(status().isOk()) // 期望 HTTP 状态码为 200 OK [4]
                    .andExpect(jsonPath("$.access").value("mocked_access_token")) // 验证 JSON 响应中的 access token [4]
                    .andExpect(jsonPath("$.refresh").value("mocked_refresh_token")); // 验证 JSON 响应中的 refresh token

            verify(authServiceImpl, times(1)).login(username, password); // 验证 authService.login 被调用
        }

        @Test
        @DisplayName("凭据无效时返回 401 Unauthorized 和错误信息")
        void shouldReturnUnauthorizedOnInvalidCredentials() throws Exception {
            // Given
            String username = "invaliduser";
            String password = "invalidpassword";

            when(authServiceImpl.login(username, password)).thenReturn(null); // 模拟登录失败

            // When & Then
            mockMvc.perform(post("/api/token")
                            .param("username", username)
                            .param("password", password)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()) // 期望 HTTP 状态码为 401 Unauthorized
                    .andExpect(jsonPath("$.error").value("Invalid credentials")); // 验证错误信息

            verify(authServiceImpl, times(1)).login(username, password);
        }

        @Test
        @DisplayName("缺少用户名参数时返回 400 Bad Request")
        void shouldReturnBadRequestWhenUsernameIsMissing() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/token")
                            .param("password", "anypassword")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()); // 期望 400 Bad Request

            verify(authServiceImpl, never()).login(anyString(), anyString()); // 验证 authService 未被调用
        }

        @Test
        @DisplayName("缺少密码参数时返回 400 Bad Request")
        void shouldReturnBadRequestWhenPasswordIsMissing() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/token")
                            .param("username", "anyuser")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()); // 期望 400 Bad Request

            verify(authServiceImpl, never()).login(anyString(), anyString()); // 验证 authService 未被调用
        }
    }

    @Nested
    @DisplayName("处理刷新 Access Token 请求时 (POST /api/token/refresh)")
    class RefreshTokenTests {

        @Test
        @DisplayName("成功刷新 Access Token 并返回新的 access token")
        void shouldReturnNewAccessTokenOnSuccessfulRefresh() throws Exception {
            // Given
            String refreshToken = "valid_refresh_token";
            String newAccessToken = "new_mocked_access_token";

            when(authServiceImpl.refreshAccessToken(refreshToken)).thenReturn(newAccessToken); // 模拟刷新成功

            // When & Then
            mockMvc.perform(post("/api/token/refresh")
                            .param("refresh", refreshToken) // 设置查询参数
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access").value("new_mocked_access_token"));

            verify(authServiceImpl, times(1)).refreshAccessToken(refreshToken); // 验证 authService.refreshAccessToken 被调用
        }

        @Test
        @DisplayName("刷新令牌无效时返回 401 Unauthorized 和错误信息")
        void shouldReturnUnauthorizedOnInvalidRefreshToken() throws Exception {
            // Given
            String refreshToken = "invalid_refresh_token";

            when(authServiceImpl.refreshAccessToken(refreshToken)).thenReturn(null); // 模拟刷新失败

            // When & Then
            mockMvc.perform(post("/api/token/refresh")
                            .param("refresh", refreshToken)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("Invalid refresh token"));

            verify(authServiceImpl, times(1)).refreshAccessToken(refreshToken);
        }

        @Test
        @DisplayName("缺少 refresh 参数时返回 400 Bad Request")
        void shouldReturnBadRequestWhenRefreshParamIsMissing() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/token/refresh")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()); // 期望 400 Bad Request

            verify(authServiceImpl, never()).refreshAccessToken(anyString()); // 验证 authService 未被调用
        }
    }
}