package com.hanul.pis.authentication.security;

import com.hanul.pis.authentication.SpringAppContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Petri ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_PATH = "/users ";

    // TOKEN_SECRET was moved to application.properties to allow it to be updated easily from time to time
    public static String getTokenSecret() {
        Environment environment = (Environment) SpringAppContext.getBean("environment");
        return environment.getProperty("token.secret");
    }

    private SecurityConstants() {
    }
}
