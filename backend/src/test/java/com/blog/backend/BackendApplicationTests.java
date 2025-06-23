package com.blog.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test; // 导入 JUnit 5 的 @Test 注解
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 如果这是一个 Spring Boot 应用的测试类，通常会用 @SpringBootTest
// @SpringBootTest
public class BackendApplicationTests {

    private static final Logger logger = LogManager.getLogger(BackendApplicationTests.class);

    // 声明 PasswordEncoder 实例
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 将密码加密和日志输出放在一个测试方法中
    @Test // 这是一个 JUnit 5 的测试方法
    void encodeAndLogPassword() {
        String rawPassword = "123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        logger.info("原始密码: {}", rawPassword);
        logger.info("加密后的密码: {}", encodedPassword);

        // 你也可以在这里添加断言来验证加密后的密码是否符合预期（例如长度，但不验证具体内容）
        // 或者验证 matches 方法
        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.info("密码是否匹配: {}", isMatch);
        // Assertions.assertTrue(isMatch); // 如果你想在这里添加断言
    }

    // 如果你需要在其他地方使用这个加密逻辑，可以封装成一个私有方法
    private String getEncodedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}