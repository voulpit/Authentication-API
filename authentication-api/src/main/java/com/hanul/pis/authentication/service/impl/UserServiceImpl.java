package com.hanul.pis.authentication.service.impl;

import com.hanul.pis.authentication.infra.entity.AddressEntity;
import com.hanul.pis.authentication.infra.entity.RoleEntity;
import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.AddressRepository;
import com.hanul.pis.authentication.infra.repo.RoleRepository;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.model.dto.shared.AddressDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.model.exception.UserValidationException;
import com.hanul.pis.authentication.security.UserPrincipal;
import com.hanul.pis.authentication.service.AuditService;
import com.hanul.pis.authentication.service.CachingService;
import com.hanul.pis.authentication.service.UserService;
import com.hanul.pis.authentication.utils.AuditEvent;
import com.hanul.pis.authentication.utils.ErrorMessages;
import com.hanul.pis.authentication.utils.RegistrationUtils;
import com.hanul.pis.authentication.utils.aws.ses.EmailVerification;
import com.hanul.pis.authentication.utils.aws.ses.PasswordReset;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hanul.pis.authentication.utils.Constants.ADDRESS_ID_LENGTH;
import static com.hanul.pis.authentication.utils.Constants.USER_ID_LENGTH;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationUtils registrationUtils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private Environment environment;
    @Autowired
    private CachingService cachingService;
    private boolean useCaching = false;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuditService auditService;

    @PostConstruct
    public void initialize() {
        String usernameCaching = environment.getProperty("enable.caching");
        if ("true".equals(usernameCaching)) {
            this.useCaching = true;
            cachingService.initialize(userRepository.getAllEmails());
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UserValidationException {
        UserEntity userEntity = null;
        UserDto response = null;

        try {
            if (useCaching) {
                if (cachingService.findUsername(userDto.getEmail())) {
                    throw new UserValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);
                }
            } else {
                UserEntity user = userRepository.findByEmail(userDto.getEmail());
                if (user != null) {
                    throw new UserValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);
                }
            }

            ModelMapper modelMapper = new ModelMapper();
            userEntity = modelMapper.map(userDto, UserEntity.class);

            if (userEntity.getAddresses() != null) {
                for (AddressEntity addressDto : userEntity.getAddresses()) {
                    addressDto.setPublicId(registrationUtils.generateAddressId(ADDRESS_ID_LENGTH));
                    addressDto.setUserEntity(userEntity);
                }
            }

            userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userEntity.setUserId(registrationUtils.generateUserId(USER_ID_LENGTH));
            userEntity.setEmailVerificationToken(registrationUtils.generateEmailVerificationToken(userEntity.getUserId()));

            List<RoleEntity> roles = new ArrayList<>();
            for (String role : userDto.getRoles()) {
                RoleEntity roleEntity = roleRepository.findByName(role);
                if (roleEntity != null) {
                    roles.add(roleEntity);
                }
            }
            userEntity.setRoles(roles);

            userEntity = userRepository.save(userEntity);

            response = modelMapper.map(userEntity, UserDto.class);
            new EmailVerification().verifyEmail(environment.getProperty("url"), response);

            if (useCaching) {
                cachingService.addUsername(userDto.getEmail());
            }

            auditService.insertAudit(userEntity, userEntity, AuditEvent.CREATE_USER, true, userDto.getEmail());

        } catch (Exception e) {
            auditService.insertAudit(userEntity, userEntity, AuditEvent.CREATE_USER, userEntity != null, userDto.getEmail() + "; " + e.getMessage());
        }

        return response;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserEntity userEntity = null;
        UserDto updatedUser = null;
        try {
            userEntity = userRepository.findByUserId(userId);
            checkUserExists(userEntity, userId);

            userEntity.setFirstName(userDto.getFirstName());
            userEntity.setLastName(userDto.getLastName());
            userEntity = userRepository.save(userEntity);

            updatedUser = new UserDto();
            BeanUtils.copyProperties(userEntity, updatedUser);

            auditService.insertAudit(getPrincipalUser(), userEntity, AuditEvent.UPDATE_USER, true, "Updated user " + userEntity.getEmail());
        } catch (Exception e) {
            auditService.insertAudit(getPrincipalUser(), userEntity, AuditEvent.UPDATE_USER, updatedUser != null, userDto.getEmail() + "; " + e.getMessage());
        }
        return updatedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        checkUserExists(userEntity, username);
        return new UserPrincipal(userEntity);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        checkUserExists(userEntity, email);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        checkUserExists(userEntity, userId);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public boolean deleteUser(String userId) {
        UserEntity userEntity = null;
        try {
            userEntity = userRepository.findByUserId(userId);
            checkUserExists(userEntity, userId);

            userEntity.setDeletedInd(true);
            userEntity = userRepository.save(userEntity);

            auditService.insertAudit(getPrincipalUser(), userEntity, AuditEvent.DELETE_USER, userEntity.getDeletedInd(), userEntity.getEmail());
            return userEntity.getDeletedInd();

        } catch (Exception e) {
            boolean deleted = userEntity != null ? userEntity.getDeletedInd() : false;
            auditService.insertAudit(getPrincipalUser(), userEntity, AuditEvent.DELETE_USER, deleted, e.getMessage());
            return deleted;
        }
    }

    @Override
    public List<AddressDto> getUserAddresses(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        checkUserExists(userEntity, userId);

        List<AddressDto> result = new ArrayList<>();
        if (userEntity.getAddresses() != null) {
            for (AddressEntity addressEntity : userEntity.getAddresses()) { // sau: addressRepository.findAllByUserEntity(userEntity);
                AddressDto addressDto = new AddressDto();
                BeanUtils.copyProperties(addressEntity, addressDto);
                result.add(addressDto);
            }
        }
        return result;
    }

    @Override
    public AddressDto getUserAddress(String userId, String addressId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        checkUserExists(userEntity, userId);

        AddressDto result = new AddressDto();
        AddressEntity address = addressRepository.findByPublicId(addressId);
        if (address == null || !userEntity.equals(address.getUserEntity())) {
            throw new UserValidationException("Invalid address provided!");
        }
        BeanUtils.copyProperties(address, result);

        return result;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        Pageable pageableRequest = PageRequest.of(page, limit);
        List<UserEntity> users = userRepository.findAll(pageableRequest).getContent();

        List<UserDto> result = new ArrayList<>();
        for (UserEntity userEntity : users) {
            if (userEntity.getDeletedInd()) {
                continue;
            }
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            result.add(userDto);
        }
        return result;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
        if (userEntity == null || RegistrationUtils.hasTokenExpired(token)) {
            return false;
        }
        userEntity.setEmailVerificationToken(null);
        userEntity.setEmailVerificationStatus(true);
        userEntity.setActiveInd(true);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public boolean requestToResetPassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return false;
        }

        String token = registrationUtils.generatePasswordResetToken(userEntity.getUserId());
        userEntity.setPasswordResetToken(token);
        userRepository.save(userEntity);

        return new PasswordReset().resetPassword(environment.getProperty("url"), userEntity.getFirstName(),
                userEntity.getEmail(), userEntity.getPasswordResetToken());
    }

    @Override
    public boolean resetPassword(String password, String token) {
        if (RegistrationUtils.hasTokenExpired(token)) {
            return false;
        }
        UserEntity userEntity = userRepository.findByPasswordResetToken(token);
        if (userEntity == null) {
            return false;
        }
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
        userEntity.setPasswordResetToken(null);
        userRepository.save(userEntity);
        return true;
    }

    private void checkUserExists(UserEntity userEntity, String searchedBy) {
        if (userEntity == null || userEntity.getDeletedInd()) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND + searchedBy);
        }
    }

    private UserEntity getPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
        return userRepository.findByUserId(userId);
    }
}
