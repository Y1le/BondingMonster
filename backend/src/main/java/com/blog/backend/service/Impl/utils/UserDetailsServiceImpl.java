package com.blog.backend.service.Impl.utils; // 建议放在这里

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; // 假设您使用 MyBatis-Plus
import com.blog.backend.entity.User;
import com.blog.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper; // 注入您的 UserMapper

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查找用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 将查询到的 User 实体封装到 UserDetailsImpl 中并返回
        return new UserDetailsImpl(user);
    }
}