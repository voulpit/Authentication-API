package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.CreateUserRequestDto;
import com.hanul.pis.authentication.model.dto.OperationStatusDto;
import com.hanul.pis.authentication.model.dto.UpdateUserInfoRequestDto;
import com.hanul.pis.authentication.model.dto.UserDetailsDto;
import com.hanul.pis.authentication.model.dto.shared.AddressDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.UserService;
import com.hanul.pis.authentication.utils.Constants;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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

    @GetMapping(path = "/{userId}/address",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AddressDto> getAddresses(@PathVariable String userId) {
        return userService.getUserAddresses(userId);
    }

    @GetMapping(path = "/{userId}/address/{addressId}",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressDto getAddress(@PathVariable String userId, @PathVariable String addressId) {
        return userService.getUserAddress(userId, addressId);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserDetailsDto createUser(@Valid @RequestBody CreateUserRequestDto userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        return modelMapper.map(createdUser, UserDetailsDto.class);
    }

    @PutMapping(path="/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserDetailsDto updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserInfoRequestDto userDetails) {
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
