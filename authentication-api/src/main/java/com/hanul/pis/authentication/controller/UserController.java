package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.SignUpRequestDto;
import com.hanul.pis.authentication.model.dto.UserDetailsDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "/{userId}",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // order matters: XML by default if "accept" header isn't defined
    public UserDetailsDto getUserDetails(@PathVariable String userId) {
        UserDetailsDto result = new UserDetailsDto();

        UserDto userDto = userService.getUserByUserId(userId);
        BeanUtils.copyProperties(userDto, result);

        return result;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserDetailsDto createUser(@RequestBody SignUpRequestDto userDetails) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDetailsDto responseDto = new UserDetailsDto();
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
