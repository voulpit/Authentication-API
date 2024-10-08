package com.hanul.pis.authentication.infra;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAllConfirmedUsers() {
        Pageable pageableRequest = PageRequest.of(0, 1);
        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        Assertions.assertNotNull(pages);
    }

    @Test
    public void testFindUserByName() {
        String firstname = "Mia", lastname = "Chitibusar";
        List<UserEntity> users = userRepository.findUserByName(firstname, lastname);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(5, users.size());
    }

    @Test
    public void testFindUserByKeyword() {
        String keyword = "Chiti";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);
        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertTrue(users.get(0).getFirstName().contains(keyword) || users.get(0).getLastName().contains(keyword));
    }

    @Test
    public void testFindUserByKeywordLessInfo() {
        String keyword = "Chiti";
        List<Object[]> users = userRepository.findUserByKeywordLessInfo(keyword);
        Assertions.assertNotNull(users);
        Assertions.assertEquals("Chitibusar", users.get(0)[1]);
    }
}
