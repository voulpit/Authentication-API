package com.hanul.pis.authentication.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {
    public static final Pattern REGEX = Pattern.compile("^[A-Z0-9._-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = REGEX.matcher(email);
        return matcher.matches();
    }
}
