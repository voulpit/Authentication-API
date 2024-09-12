package com.hanul.pis.authentication.controller.advice;

import com.hanul.pis.authentication.model.exception.ErrorMessageDto;
import com.hanul.pis.authentication.model.exception.UserValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = {UserValidationException.class})
    public ResponseEntity<Object> handleUserServiceException(UserValidationException exception) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(exception.getMessage(), new Date(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleCustomValidationException(MethodArgumentNotValidException exception) {
        Optional<String> errorMessages = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList().stream().reduce((str1, str2) -> str1 + "; " + str2);

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(errorMessages.orElse("Unknown error"), new Date(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception exception) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(exception.getMessage(), new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
