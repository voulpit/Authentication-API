package com.hanul.pis.authentication.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailValidation {

    public String message() default "Invalid email: acceptable characters include a-z A-Z 0-9 . - _"; // error message

    public Class<?>[] groups() default {}; // group of constraints

    public Class<? extends Payload>[] payload() default {}; // additional info
}
