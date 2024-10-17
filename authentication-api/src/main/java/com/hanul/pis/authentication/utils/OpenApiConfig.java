package com.hanul.pis.authentication.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String authHeader = "Authorization";
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(authHeader,
                                // The token (after Bearer) will have to be added by pressing Authorize
                                // No need to set other headers
                                new SecurityScheme()
                                        .name(authHeader)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
                .addSecurityItem(new SecurityRequirement().addList(authHeader))
                .info(new Info()
                        .title("User Authentication")
                        .description("REST API for users")
                        .version("1.0"));
    }
}
