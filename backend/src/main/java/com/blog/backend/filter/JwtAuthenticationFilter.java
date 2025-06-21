package com.blog.backend.filter;

import com.blog.backend.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 检查请求路径是否是公共路径 (permitAll() 的路径)
        // 这些路径不应该被 JWT 过滤器拦截或强制要求 JWT
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/token") || // 匹配 /api/token 和 /api/token/refresh
                requestUri.equals("/myspace/getinfo") || // 如果这个也需要公开访问
                requestUri.equals("/api/user/topUsers")) { // 如果这个也需要公开访问
            filterChain.doFilter(request, response);
            return; // 直接放行，不执行后续的 JWT 验证逻辑
        }

        // 2. 只有当请求不是公共路径时，才尝试解析和验证 JWT
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        Long userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwt);
                userId = jwtUtil.getUserIdFromToken(jwt);
            } catch (Exception e) {
                // JWT 解析失败或过期，记录警告但不要阻止过滤器链继续
                // Spring Security 的 ExceptionTranslationFilter 会处理认证失败
                logger.warn("JWT Token is invalid or expired: " + e.getMessage());
                // 如果这里不设置认证信息，后续的 authenticated() 规则会拒绝请求
                // 对于需要认证的路径，如果JWT无效，这里不设置认证信息是正确的行为
            }
        }

        // 3. 如果 JWT 解析成功且当前没有认证信息，则设置认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateAccessToken(jwt)) { // 再次验证 JWT 是否有效
                UserDetails userDetails = new User(username, "", new ArrayList<>());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                request.setAttribute("userId", userId);
            }
        }

        // 4. 继续过滤器链
        filterChain.doFilter(request, response);
    }
}