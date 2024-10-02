package com.hanul.pis.authentication.utils;

public class Constants {
    public static final int USER_ID_LENGTH = 30;
    public static final int ADDRESS_ID_LENGTH = 20;
    public enum Operation {
        DELETE,
        VERIFY_EMAIL,
        REQUEST_PASSWORD_RESET,
        PASSWORD_RESET
    };

    private Constants() {
    }
}
