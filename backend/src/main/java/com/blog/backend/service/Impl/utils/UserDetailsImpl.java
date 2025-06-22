// package com.blog.backend.service.Impl.utils; (或者您希望的任何包)
package com.blog.backend.service.Impl.utils; // 建议放在这里

import com.blog.backend.entity.User; // 导入您的 User 实体
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private User user; // 包装您的 User 实体

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() { // 提供一个方法来获取原始的 User 实体
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 这里返回用户的权限列表。
        // 假设您的 User 实体中有一个字段（例如 roles）存储了用户的角色，
        // 或者您想给所有用户默认分配一个角色。
        // 示例：如果您的 User 实体有一个 List<String> roles 字段
        // return user.getRoles().stream()
        //         .map(SimpleGrantedAuthority::new)
        //         .collect(Collectors.toList());
        // 如果没有特定角色，或者您想简化，可以返回一个空列表或默认角色
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // 示例：给所有用户一个默认角色
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 返回您的 User 实体中的加密密码
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // 返回您的 User 实体中的用户名
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户是否过期，根据您的业务逻辑
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账户是否锁定，根据您的业务逻辑
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭据是否过期，根据您的业务逻辑
    }

    @Override
    public boolean isEnabled() {
        return true; // 账户是否启用，根据您的业务逻辑
    }
}