
package com.blog.backend.service.Impl.utils; // 建议放在这里

import com.blog.backend.entity.User; // 导入您的 User 实体
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class    UserDetailsImpl implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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