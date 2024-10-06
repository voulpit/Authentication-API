package com.hanul.pis.authentication.service;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetUserByEmail_found() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Anna");
        userEntity.setEncryptedPassword("hdh65f8@iugiur.uiy5gf54djn");
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto result = userService.getUserByEmail("email@email.ro");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userEntity.getId(), result.getId());
        Assertions.assertEquals(userEntity.getFirstName(), result.getFirstName());
        Assertions.assertEquals(userEntity.getEncryptedPassword(), result.getEncryptedPassword());
    }

    @Test
    public void testGetUserByEmail_not_found() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail("email@email.ro"));
    }
}
