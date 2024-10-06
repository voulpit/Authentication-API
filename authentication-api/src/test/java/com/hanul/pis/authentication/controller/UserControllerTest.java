package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.UserDetailsDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetUserDetails() {
        UserDto userDto = new UserDto();
        userDto.setEmail("abcdefg@yahoo.com");
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserDetailsDto result = userController.getUserDetails("6756hhghghdfdfg");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("abcdefg@yahoo.com", result.getEmail());
    }
}
