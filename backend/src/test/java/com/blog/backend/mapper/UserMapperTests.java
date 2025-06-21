package com.blog.backend.mapper;

import com.blog.backend.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // 确保导入
import org.springframework.transaction.annotation.Transactional; // 确保导入

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID; // 确保导入

import static org.assertj.core.api.Assertions.assertThat; // 使用 AssertJ 更流畅的断言

@SpringBootTest // 启动 Spring Boot 上下文
@Transactional // 每个测试方法都在事务中运行，并在结束后回滚，确保数据隔离
@ActiveProfiles("test") // 激活 application-test.yml 中配置的 H2 内存数据库
@DisplayName("UserMapper 集成测试") // 明确测试目标
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    // 移除了 @BeforeEach cleanUp 方法，因为 @Transactional 会自动回滚，无需手动清理

    @Test
    @DisplayName("通过不存在的用户名查询，应返回null")
    public void selectByUsername_returnsNullForNonExistentUser() {
        // 使用 UUID 确保查询的用户名是唯一的，不会与数据库中已有的数据冲突
        String nonExistentUsername = "nonexistent_user_" + UUID.randomUUID().toString().substring(0, 8);
        User user = userMapper.selectByUsername(nonExistentUsername);
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("通过不存在的ID查询，应返回null")
    public void selectById_returnsNullForNonExistentId() {
        // 假设 -1L 不会是有效用户ID
        Long nonExistentId = -1L;
        User user = userMapper.selectById(nonExistentId);
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("插入用户并能通过ID查询到")
    public void insertUserAndGetById() {
        // 创建测试用户，使用 UUID 确保用户名唯一，避免 Duplicate Entry 错误
        String uniqueUsername = "testuser_mapper_" + UUID.randomUUID().toString().substring(0, 8);
        User testUser = new User();
        testUser.setUsername(uniqueUsername);
        testUser.setPassword("testpass123_encoded"); // 使用模拟的加密密码字符串
        testUser.setPhoto("test_photo_url");
        testUser.setFollowerCount(0);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());

        // 插入用户
        int insertResult = userMapper.insertUser(testUser);
        assertThat(insertResult).isEqualTo(1); // 验证插入成功，返回影响的行数

        // 验证用户ID是否已生成（MyBatis 的 useGeneratedKeys）
        assertThat(testUser.getId()).isNotNull();

        // 通过ID查询刚插入的用户
        User insertedUser = userMapper.selectById(testUser.getId());
        assertThat(insertedUser).isNotNull();
        assertThat(insertedUser.getUsername()).isEqualTo(uniqueUsername); // 断言使用动态生成的用户名
        assertThat(insertedUser.getPhoto()).isEqualTo("test_photo_url");
        assertThat(insertedUser.getFollowerCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("更新用户粉丝数，应能正确增加和减少")
    public void updateFollowerCount_increaseAndDecrease() {
        // 创建测试用户，使用 UUID 确保用户名唯一
        String uniqueUsername = "testuser_follower_" + UUID.randomUUID().toString().substring(0, 8);
        User testUser = new User();
        testUser.setUsername(uniqueUsername);
        testUser.setPassword("testpass123_encoded");
        testUser.setPhoto("test_photo_url");
        testUser.setFollowerCount(0);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());

        // 插入用户
        userMapper.insertUser(testUser);
        Long userId = testUser.getId(); // 获取插入后生成的ID

        // 初始粉丝数应为0
        User userBeforeUpdate = userMapper.selectById(userId);
        assertThat(userBeforeUpdate.getFollowerCount()).isEqualTo(0);

        // 增加粉丝数
        int increaseResult = userMapper.updateFollowerCount(userId, 5);
        assertThat(increaseResult).isEqualTo(1); // 验证更新成功

        // 验证粉丝数增加
        User userAfterIncrease = userMapper.selectById(userId);
        assertThat(userAfterIncrease.getFollowerCount()).isEqualTo(5);

        // 减少粉丝数
        int decreaseResult = userMapper.updateFollowerCount(userId, -2);
        assertThat(decreaseResult).isEqualTo(1); // 验证更新成功

        // 验证粉丝数减少
        User userAfterDecrease = userMapper.selectById(userId);
        assertThat(userAfterDecrease.getFollowerCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("查询热门用户榜单，应按粉丝数降序排列并返回正确数量")
    public void selectTopUsers_returnsPopularUsers() {
        // 创建多个测试用户并设置不同的粉丝数，确保用户名唯一
        User user1 = new User();
        String uniqueUsername1 = "top_user1_" + UUID.randomUUID().toString().substring(0, 8);
        user1.setUsername(uniqueUsername1);
        user1.setPassword("testpass123_encoded");
        user1.setPhoto("test_photo_url1");
        user1.setFollowerCount(100); // 粉丝数最高
        user1.setCreateTime(LocalDateTime.now());
        user1.setUpdateTime(LocalDateTime.now());
        userMapper.insertUser(user1);

        User user2 = new User();
        String uniqueUsername2 = "top_user2_" + UUID.randomUUID().toString().substring(0, 8);
        user2.setUsername(uniqueUsername2);
        user2.setPassword("testpass123_encoded");
        user2.setPhoto("test_photo_url2");
        user2.setFollowerCount(80); // 粉丝数次高
        user2.setCreateTime(LocalDateTime.now());
        user2.setUpdateTime(LocalDateTime.now());
        userMapper.insertUser(user2);

        User user3 = new User();
        String uniqueUsername3 = "top_user3_" + UUID.randomUUID().toString().substring(0, 8);
        user3.setUsername(uniqueUsername3);
        user3.setPassword("testpass123_encoded");
        user3.setPhoto("test_photo_url3");
        user3.setFollowerCount(60); // 粉丝数最低
        user3.setCreateTime(LocalDateTime.now());
        user3.setUpdateTime(LocalDateTime.now());
        userMapper.insertUser(user3);

        // 查询前2名用户
        List<User> top2Users = userMapper.selectTopUsers(2);
        assertThat(top2Users).hasSize(2); // 验证返回数量

        // 验证排名顺序和用户名是否正确
        // 确保返回的用户是按照 followerCount 降序排列的
        assertThat(top2Users.get(0).getUsername()).isEqualTo(user1.getUsername());
        assertThat(top2Users.get(0).getFollowerCount()).isEqualTo(100); // 也可以断言粉丝数

        // 确保这里是 user2.getUsername() 而不是 "top_user2"
        assertThat(top2Users.get(1).getUsername()).isEqualTo(user2.getUsername());
        assertThat(top2Users.get(1).getFollowerCount()).isEqualTo(80);
    }
}