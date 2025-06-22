package com.blog.backend.service.Impl.user.account;

import com.blog.backend.entity.User;
import com.blog.backend.service.Impl.utils.UserDetailsImpl; // 确保导入了正确的 UserDetailsImpl 包
import com.blog.backend.service.user.account.AuthService;
import com.blog.backend.util.JwtUtil;
import com.blog.backend.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException; // 导入 AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    // 构造函数注入所有依赖
    public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 包含 access 和 refresh token 和 id 的 Map，或 null (认证失败)
     */
    @Override // 确保实现了接口方法
    public Map<String, String> login(String username, String password) {
        Map<String, String> result = new HashMap<>(); // 用于存储结果的 Map

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            // 尝试进行认证
            // 这一行是认证的核心，如果 UserDetailsService 或其他地方存在循环，StackOverflowError 会在这里发生
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            // 认证成功后，获取 UserDetailsImpl 对象
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            User user = loginUser.getUser(); // 从 UserDetailsImpl 中获取原始的 User 实体

            // 生成 JWT token
            String access = JwtUtil.createJWT(user.getId().toString());
            String refresh = JwtUtil.createRJWT(user.getId().toString());

            result.put("success", "true"); // 认证成功标志
            result.put("access", access);
            result.put("refresh", refresh);
            result.put("id", String.valueOf(user.getId()));

        } catch (AuthenticationException e) {
            // 认证失败，Spring Security 会抛出 AuthenticationException 及其子类
            // 例如：BadCredentialsException (密码错误), UsernameNotFoundException (用户不存在)
            result.put("success", "false"); // 认证失败标志
            result.put("message", e.getMessage()); // 返回具体的错误信息
            // 打印堆栈跟踪以便调试，但在生产环境中可能需要更优雅的错误处理
            e.printStackTrace();
        } catch (Exception e) {
            // 捕获其他可能的异常，例如 ClassCastException 如果 UserDetailsImpl 没有正确返回
            result.put("success", "false");
            result.put("message", "登录过程中发生未知错误: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 刷新 Access Token
     * @param refreshToken 刷新令牌
     * @return 新的 Access Token，或 null (刷新失败)
     */
    @Override // 确保实现了接口方法
    public String refreshAccessToken(String refreshToken) {
        try {
            return JwtUtil.refreshAccessToken(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}