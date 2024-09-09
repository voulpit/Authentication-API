package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.OperationStatusDto;
import com.hanul.pis.authentication.model.dto.UserDetailsDto;
import com.hanul.pis.authentication.model.dto.UserDetailsRequestDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.UserService;
import com.hanul.pis.authentication.utils.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public UserDetailsDto createUser(@RequestBody UserDetailsRequestDto userDetails) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDetailsDto responseDto = new UserDetailsDto();
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, responseDto);

        return responseDto;
    }

    @PutMapping(path="/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserDetailsDto updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestDto userDetails) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDetailsDto responseDto = new UserDetailsDto();
        UserDto updatedUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updatedUser, responseDto);

        return responseDto;
    }

    @DeleteMapping(path="/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusDto deleteUser(@PathVariable String userId) {
        OperationStatusDto operationStatus = new OperationStatusDto();
        operationStatus.setUserId(userId);
        operationStatus.setOperationName(Constants.Operation.DELETE);
        operationStatus.setSuccessful(userService.deleteUser(userId));
        return operationStatus;
    }

    /**
     * Paginated query
     * Pagination starts from 0
     * @return undeleted users
     */
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserDetailsDto> getUsers(@RequestParam(value="page", defaultValue = "0") int page,
                                         @RequestParam(value="limit", defaultValue = "10") int limit) {
        List<UserDetailsDto> result = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);
        for (UserDto userDto : users) {
            UserDetailsDto userDetails = new UserDetailsDto();
            BeanUtils.copyProperties(userDto, userDetails);
            result.add(userDetails);
        }

        return result;
    }
}
