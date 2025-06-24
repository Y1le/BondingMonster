package com.blog.backend.service.user.account;

import java.util.Map;

public interface AuthService {
    public Map<String, String> login(String username, String password) ;
    public String refreshAccessToken(String refreshToken);
    public Map<String, String> register(String username, String password) ;
}
