package com.hanul.pis.authentication.security;

import com.hanul.pis.authentication.SpringAppContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final long PASSWORD_RESET_EXP_TIME = 3600*1000; // 1 hour
    public static final String TOKEN_PREFIX = "Petri ";
    public static final String HEADER_STRING = "Authorization";

    public static final String SIGN_UP_PATH = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
    public static final String H2_CONSOLE = "/h2-console/**";
    public static final String ERROR_URL = "/error";

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String INSPECTOR_ROLE = "INSPECTOR"; // can view audit

    public static final String READ_AUTHORITY = "READ_AUTH";
    public static final String WRITE_AUTHORITY = "WRITE_AUTH";
    public static final String DELETE_AUTHORITY = "DELETE_AUTH";
    public static final String AUDIT_AUTHORITY = "AUDIT_AUTH";

    // TOKEN_SECRET was moved to application.properties to allow it to be updated easily from time to time
    public static String getTokenSecret() {
        Environment environment = (Environment) SpringAppContext.getBean("environment");
        return environment.getProperty("token.secret");
    }

    private SecurityConstants() {
    }
}
