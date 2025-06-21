package com.blog.backend.controller;

import com.blog.backend.service.AuthService;
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
     * Request Body: username, password
     */
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
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
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refresh) {
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
}