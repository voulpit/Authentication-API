package com.hanul.pis.authentication.utils;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field"),
    NO_RECORD_FOUND("Record not found: "),
    EMAIL_ALREADY_EXISTS("Email already exists!"),
    AUTHENTICATION_FAILED("Authentication failed"),
    INTERNAL_SERVER_ERROR("Internal server error");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
