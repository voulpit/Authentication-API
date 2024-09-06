package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.SignUpRequestDto;
import com.hanul.pis.authentication.model.dto.SignUpResponseDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String getUser() {
        return "Got";
    }

    @PostMapping
    public SignUpResponseDto createUser(@RequestBody SignUpRequestDto userDetails) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        SignUpResponseDto responseDto = new SignUpResponseDto();
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, responseDto);

        return responseDto;
    }

    @PutMapping
    public String updateUser() {
        return "Updated";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Deleted";
    }
}
