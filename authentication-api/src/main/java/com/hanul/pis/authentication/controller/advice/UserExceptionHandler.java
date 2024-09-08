package com.hanul.pis.authentication.controller.advice;

import com.hanul.pis.authentication.model.exception.ErrorMessageDto;
import com.hanul.pis.authentication.model.exception.UserValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = {UserValidationException.class})
    public ResponseEntity<Object> handleUserServiceException(UserValidationException exception, WebRequest request) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(exception.getMessage(), new Date(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception exception, WebRequest request) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(exception.getMessage(), new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
