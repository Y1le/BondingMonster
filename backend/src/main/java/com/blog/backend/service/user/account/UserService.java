package com.blog.backend.service.user.account; // 保持您原有的接口包路径

import com.blog.backend.entity.User;
import java.util.List;

public interface UserService {

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    User getUserInfo(Long userId);

    /**
     * 获取粉丝数排名前N的用户
     * @param limit 限制数量
     * @return 用户列表
     */
    List<User> getTopUsers(int limit);

    /**
     * 更新用户的粉丝数
     * @param userId 用户ID
     * @param count 要更新的粉丝数（可以是增量或绝对值，取决于您的mapper实现）
     */
    void updateFollowerCount(Long userId, Integer count);
}