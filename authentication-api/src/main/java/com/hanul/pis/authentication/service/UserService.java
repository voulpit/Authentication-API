package com.hanul.pis.authentication.service;

import com.hanul.pis.authentication.model.dto.shared.AddressDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(String userId, UserDto userDto);
    boolean deleteUser(String userId);
    UserDto getUserByEmail(String email);
    UserDto getUserByUserId(String userId);
    List<AddressDto> getUserAddresses(String userId);
    AddressDto getUserAddress(String userId, String addressId);
    List<UserDto> getUsers(int page, int limit);
    boolean verifyEmailToken(String token);
}
