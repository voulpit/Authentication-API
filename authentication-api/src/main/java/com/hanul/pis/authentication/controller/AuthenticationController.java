package com.hanul.pis.authentication.controller;

import com.hanul.pis.authentication.model.dto.LoginRequestDto;
import com.hanul.pis.authentication.security.SecurityConstants;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fake controller
 * Swagger does not generate documentation for the login
 * Using this controller it will show the documentation for /get-me-in endpoint
 */
@RestController
public class AuthenticationController {

    @ApiResponses(value = {
            @ApiResponse(description = "Response headers",
                         headers = {@Header(name = "Authorization", description = "Bearer JWT"),
                                    @Header(name = "UserId", description = "Public UserId")})
    })
    @PostMapping(SecurityConstants.LOGIN_PATH)
    public void fakeLogin(@RequestBody LoginRequestDto loginRequestDto) {
        throw new IllegalStateException("This should not be called. Login is implemented in AuthenticationFilter.");
    }
}
