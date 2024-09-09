package com.hanul.pis.authentication.service.impl;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.model.exception.UserValidationException;
import com.hanul.pis.authentication.service.UserService;
import com.hanul.pis.authentication.utils.ErrorMessages;
import com.hanul.pis.authentication.utils.RegistrationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hanul.pis.authentication.utils.Constants.USER_ID_LENGTH;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationUtils registrationUtils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) throws UserValidationException {
        validateInputForUserCreation(userDto);

        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        if (user != null) {
            throw new UserValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(registrationUtils.generateUserId(USER_ID_LENGTH));
        userEntity = userRepository.save(userEntity);

        UserDto storedUser = new UserDto();
        BeanUtils.copyProperties(userEntity, storedUser);
        return storedUser;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        validateInputForUserUpdate(userDto);

        UserEntity userEntity = userRepository.findByUserId(userId);
        checkUserExists(userEntity, userId);

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity = userRepository.save(userEntity);

        UserDto updatedUser = new UserDto();
        BeanUtils.copyProperties(userEntity, updatedUser);
        return updatedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        checkUserExists(userEntity, username);
        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
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
        UserEntity userEntity = userRepository.findByUserId(userId);
        checkUserExists(userEntity, userId);

        userEntity.setDeletedInd(true);
        userEntity = userRepository.save(userEntity);
        return userEntity.getDeletedInd();
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

    private void checkUserExists(UserEntity userEntity, String searchedBy) {
        if (userEntity == null || userEntity.getDeletedInd()) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getMessage() + searchedBy);
        }
    }

    private void validateInputForUserCreation(UserDto userDto) {
        if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty() ||
            userDto.getLastName() == null || userDto.getLastName().isEmpty() ||
            userDto.getEmail() == null || userDto.getEmail().isEmpty() ||
            userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new UserValidationException(ErrorMessages.MISSING_REQUIRED_FIELD.getMessage());
        }
    }

    private void validateInputForUserUpdate(UserDto userDto) {
        if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty() || userDto.getLastName() == null || userDto.getLastName().isEmpty()) {
            throw new UserValidationException(ErrorMessages.MISSING_REQUIRED_FIELD.getMessage());
        }
    }
}
