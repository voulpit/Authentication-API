package com.hanul.pis.authentication.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest // IT test
public class RegistrationUtilsTest {
    @Autowired
    private RegistrationUtils registrationUtils;

    @Test
    public void testGenerateUserId() {
        String userId = registrationUtils.generateUserId(30);
        Assertions.assertNotNull(userId);
        Assertions.assertEquals(30, userId.length());
    }

    @Test
    public void testHasTokenNotExpired() {
        String token = new RegistrationUtils().generateEmailVerificationToken("rdjdhkjf");
        Assertions.assertNotNull(token);
        boolean hasTokenExpired = RegistrationUtils.hasTokenExpired(token);
        Assertions.assertFalse(hasTokenExpired);
    }

    @Test
    public void testHasTokenExpired() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZGpkaGtqZiIsImV4cCI6MTcyNzk3NTczOX0.qXZxjIgR0ZU3IRIzYa7m8vDz7xinpnINm8J8VwuHW0dM31dp8rbndBEi51U7g8LpqcBC65rlT5hnBdojzdRPHw";
        boolean hasTokenExpired = RegistrationUtils.hasTokenExpired(token);
        Assertions.assertTrue(hasTokenExpired);
    }
}
