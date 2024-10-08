package com.hanul.pis.authentication.security;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import com.hanul.pis.authentication.infra.repo.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;

/**
 * Checks the token used for various (confidential) requests and puts it in the SecurityContext, if valid
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response); // pass the execution to the next filter in chain
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    /**
     * Verifies the token
     * 403 Unauthorized if the token is invalid
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        if (authorizationHeader == null) {
            return null;
        }

        String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
        byte [] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Jwt<Header, Claims> jwt = (Jwt<Header, Claims>) jwtParser.parse(token);
        String subject = jwt.getPayload().getSubject(); // <-------- retrieve the subject (= username, email address)
        if (subject == null) {
            return null;
        }

        // validation is successful
        UserEntity userEntity = userRepository.findByEmail(subject);
        UserPrincipal userPrincipal = new UserPrincipal(userEntity);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }
}
