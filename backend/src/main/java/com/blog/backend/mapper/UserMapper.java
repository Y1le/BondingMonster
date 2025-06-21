package com.blog.backend.mapper;


import com.blog.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User selectById(@Param("id") Long id);

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    int insertUser(User user);

    /**
     * 更新用户粉丝数
     * @param id 用户ID
     * @param count 增加或减少的数量 (正数增加，负数减少)
     * @return 影响的行数
     */
    int updateFollowerCount(@Param("id") Long id, @Param("count") Integer count);

    /**
     * 查询粉丝数排名前N的用户
     * @param limit 限制数量
     * @return 用户列表
     */
    List<User> selectTopUsers(@Param("limit") int limit);

}