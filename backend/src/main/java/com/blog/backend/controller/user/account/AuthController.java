package com.blog.backend.controller.user.account;

import com.blog.backend.controller.DTO.LoginRequest;
import com.blog.backend.controller.DTO.RefreshTokenRequest;
import com.blog.backend.controller.DTO.RegisterRequest;
import com.blog.backend.service.user.account.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     * URL: /api/token/
     * Method: POST
     * Request Body: username, password, id
     */
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Map<String, String> tokens = authService.login(username, password);
        if (tokens != null) {
            return ResponseEntity.ok(tokens);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }





    /**
     * 刷新 Access Token
     * URL: /api/token/refresh/
     * Method: POST
     * Request Body: refresh
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refresh = refreshTokenRequest.getRefresh();
        String newAccessToken = authService.refreshAccessToken(refresh);
        if (newAccessToken != null) {
            Map<String, String> response = new HashMap<>();
            response.put("access", newAccessToken);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * 用户注册
     * URL: /api/register/
     * Method: POST
     * Body: username, password
     * Request Body: message
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> Register(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        return ResponseEntity.ok(authService.register(username, password));
    }
}