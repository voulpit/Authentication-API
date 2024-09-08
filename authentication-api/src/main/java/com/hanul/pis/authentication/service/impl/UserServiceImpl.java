package com.hanul.pis.authentication.service.impl;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.utils.ErrorMessages;
import com.hanul.pis.authentication.model.exception.UserValidationException;
import com.hanul.pis.authentication.service.UserService;
import com.hanul.pis.authentication.utils.RegistrationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        validateInput(userDto);

        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        if (user != null) {
            throw new UserValidationException("Email already exists!");
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    private void validateInput(UserDto userDto) {
        if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty() ||
            userDto.getLastName() == null || userDto.getLastName().isEmpty() ||
            userDto.getEmail() == null || userDto.getEmail().isEmpty() ||
            userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new UserValidationException(ErrorMessages.MISSING_REQUIRED_FIELD.getMessage());
        }
    }
}
