package com.blog.backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 启动 Spring Boot 上下文
@Transactional
public class MyBatisTest {

//    @Test
//    void testSqlSession() {
//        // 获取SqlSession
//        try (SqlSession session = MyBatisUtil.getSqlSession()) {
//            // 测试获取UserMapper
//            UserMapper userMapper = session.getMapper(UserMapper.class);
//            assertThat(userMapper).isNotNull();
//
//            // 验证查询不存在的用户
//            assertThat(userMapper.selectByUsername("nonexistent_user")).isNull();
//        } catch (Exception e) {
//            throw new RuntimeException("Error testing MyBatis", e);
//        }
//    }
}