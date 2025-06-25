package com.blog.backend.controller.user.account;

import com.blog.backend.entity.User;
import com.blog.backend.service.user.account.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取用户信息
     * URL: /myspace/getinfo
     * Method: GET
     * Query Params: user_id
     * Headers: Authorization: Bearer <access_token>
     */
    @GetMapping("/myspace/getinfo")
    public ResponseEntity<Map<String, String>> getUserInfo(@RequestParam("user_id") Long userId) {
        return userService.getUserInfo(userId);
    }

    /**
     * 获取用户榜单
     * URL: /api/user/topUsers
     * Method: GET
     */
    @GetMapping("/api/user/topUsers")
    public ResponseEntity<Map<String, Object>> getTopUsers() {
        // 默认获取前5名用户，可以根据需求调整
        List<User> topUsers = userService.getTopUsers(5);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", topUsers); // 返回用户列表
        return ResponseEntity.ok(response);
    }
}