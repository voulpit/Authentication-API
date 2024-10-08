package com.hanul.pis.authentication.security;

import com.hanul.pis.authentication.infra.repo.UserRepository;
import com.hanul.pis.authentication.service.AuditService;
import com.hanul.pis.authentication.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // enable usage for @Secured, @PreAuthorize, @PostAuthorize
@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                       AuditService auditService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // customize login URL (default is /login)
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, auditService);
        authenticationFilter.setFilterProcessesUrl("/get-me-in");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityConstants.PASSWORD_RESET_REQUEST_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // Chrome first sends an Options request....must be successful
                        .requestMatchers(HttpMethod.DELETE).hasAuthority(SecurityConstants.DELETE_AUTHORITY)
                        .requestMatchers(SecurityConstants.ERROR_URL).permitAll() // shows 500 when it's 500, instead of 403
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .addFilter(authenticationFilter)
                .addFilter(new AuthorizationFilter(authenticationManager, userRepository))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
