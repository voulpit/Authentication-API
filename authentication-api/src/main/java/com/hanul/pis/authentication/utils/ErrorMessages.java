package com.hanul.pis.authentication.utils;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field"),
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
