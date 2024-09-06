package com.hanul.pis.authentication.service;

import com.hanul.pis.authentication.model.dto.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
}
