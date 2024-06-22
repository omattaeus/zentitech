package com.compilou.regex.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API for Registering Users")
                        .version("v1")
                        .description("API for registering users!")
                        .termsOfService("https://github.com/omattaeus/")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/omattaeus/")));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/auth/**",
                                "/users/**/**/**")
            .build();
    }
}