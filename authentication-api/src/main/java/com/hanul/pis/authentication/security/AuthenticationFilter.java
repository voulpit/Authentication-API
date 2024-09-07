package com.hanul.pis.authentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanul.pis.authentication.SpringAppContext;
import com.hanul.pis.authentication.model.dto.LoginRequestDto;
import com.hanul.pis.authentication.model.dto.shared.UserDto;
import com.hanul.pis.authentication.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto loginInfo = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginInfo.getEmail(), loginInfo.getPassword(), new ArrayList<>()));
            // first it calls UserServiceImpl::loadUserByUsername and checks the UserDetails info against those provided above
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Generates a token upon login, based on defined constants
    Also sends back the "official" userId
    Both info are included in the headers
    */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();
        String username = ((User) authResult.getPrincipal()).getUsername();

        String token = Jwts.builder()
                .subject(username)          // <-------- subject is the username (email address)
                .issuedAt(Date.from(now))
                .expiration(Date.from((now.plusMillis(SecurityConstants.EXPIRATION_TIME))))
                .signWith(secretKey)
                .compact();
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

        UserService userService = (UserService) SpringAppContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUser(username);
        response.addHeader("UserId", userDto.getUserId());
    }
}
