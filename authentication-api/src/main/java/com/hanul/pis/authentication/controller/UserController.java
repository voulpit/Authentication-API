package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.*;
import com.hanul.pis.authentication.model.dto.shared.AddressDto;
import com.hanul.pis.authentication.model.dto.shared.AuditDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.security.SecurityConstants;
import com.hanul.pis.authentication.service.AuditService;
import com.hanul.pis.authentication.service.UserService;
import com.hanul.pis.authentication.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuditService auditService;

    @Operation(description = "Get user details")
    @Secured({SecurityConstants.USER_ROLE, SecurityConstants.ADMIN_ROLE})
    @GetMapping(path = "/{userId}",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // order matters: XML by default if "accept" header isn't defined
    public UserDetailsDto getUserDetails(@PathVariable String userId) {
        UserDetailsDto result = new UserDetailsDto();

        UserDto userDto = userService.getUserByUserId(userId);
        BeanUtils.copyProperties(userDto, result);

        return result;
    }

    @Operation(description = "Get user addresses")
    @GetMapping(path = "/{userId}/addresses",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<AddressDto> getAddresses(@PathVariable String userId) {
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addresses").withSelfRel();
        CollectionModel<AddressDto> result = CollectionModel.of(userService.getUserAddresses(userId), userLink, selfLink);

        result.getContent().forEach(addr -> {
            Link selfLink2 = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("address").slash(addr.getPublicId()).withSelfRel();
            addr.add(selfLink2);
        });

        return result;
    }

    @Operation(description = "Get address for user")
    @GetMapping(path = "/{userId}/address/{addressId}",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressDto getAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressDto result = userService.getUserAddress(userId, addressId);

        // HATEOAS method 1:
        // http://locahost:8080/users/<userId> , link name is "user"
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
        // http://locahost:8080/users/<userId>/addresses , link name is "addresses"
        Link addrLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");
        // Link addrLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(userId)).withRel("addresses");
        // http://locahost:8080/users/<userId>/address/<addressId> , link name is "self"
        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("address").slash(addressId).withSelfRel();
        // Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddress(userId, addressId)).withSelfRel();
        result.add(userLink);
        result.add(addrLink);
        result.add(selfLink);

        return result;
    }

    @Operation(description = "Create new user")
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<UserDetailsDto> createUser(@Valid @RequestBody CreateUserRequestDto userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto.setRoles(List.of(SecurityConstants.USER_ROLE));

        UserDto createdUser = userService.createUser(userDto);
        UserDetailsDto result = modelMapper.map(createdUser, UserDetailsDto.class);

        // HATEOAS method 2:
        // http://locahost:8080/users/<userId>
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(result.getUserId()).withRel("user-details");
        // http://locahost:8080/users/<userId>/addresses
        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel();

        return EntityModel.of(result, Arrays.asList(userLink, selfLink));
    }

    @Operation(description = "Update user")
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

    @Operation(description = "Delete user")
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
    @Operation(description = "Get list of users")
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

    @Operation(description = "Verify user email")
    /* After user clicks the verification link in his email ~ /users/email-verification?token=skjdkjdfkld */
    @GetMapping(path = "email-verification", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusDto verifyEmailToken(@RequestParam(value="token") String token) {
        OperationStatusDto result = new OperationStatusDto();
        result.setOperationName(Constants.Operation.VERIFY_EMAIL);

        boolean isVerified = userService.verifyEmailToken(token);
        result.setSuccessful(isVerified);

        return result;
    }

    @Operation(description = "Request to change user password")
    @GetMapping(path = "password-reset-request", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusDto requestResetPassword(@RequestParam(value="email") String email) {
        OperationStatusDto result = new OperationStatusDto();
        result.setOperationName(Constants.Operation.REQUEST_PASSWORD_RESET);

        boolean done = userService.requestToResetPassword(email);
        result.setSuccessful(done);

        return result;
    }

    @Operation(description = "User password change")
    @PostMapping(path = "password-reset", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusDto resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        OperationStatusDto result = new OperationStatusDto();
        result.setOperationName(Constants.Operation.PASSWORD_RESET);

        boolean done = userService.resetPassword(passwordResetDto.getPassword(), passwordResetDto.getToken());
        result.setSuccessful(done);

        return result;
    }

    @Operation(description = "Inspect audit history for a specific user")
    @Secured(SecurityConstants.INSPECTOR_ROLE)
    // @PreAuthorize("hasRole('INSPECTOR') or #userId == principal.userId") // inspector or user who checks his own history
    // (#userId = the path variable, principal = the currently logged user)
    // @PreAuthorize("hasAuthority('AUDIT_AUTH')")
    // @PostAuthorize("returnObject.size > 0")
    @GetMapping(path = "/audit/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AuditDto> getAuditHistoryForUser(@PathVariable String userId) {
        return auditService.getAuditForAffectedUser(userId);
    }
}
