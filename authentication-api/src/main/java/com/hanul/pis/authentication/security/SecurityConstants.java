package com.hanul.pis.authentication.security;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Petri ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_PATH = "/users ";
    public static final String TOKEN_SECRET = "4348fjdj84%kfnnfh4#@@@kksdo!kdc98emruug!fkjfjjvnvuyuy@#$kjkvjkv*&0skf3455f4jhfhf83ff3jkjfkjfj337j83!"; // 100 char

    private SecurityConstants() {
    }
}
