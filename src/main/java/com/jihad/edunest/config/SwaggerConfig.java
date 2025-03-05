package com.jihad.edunest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EduNest API Documentation")
                        .version("1.0")
                        .description("API documentation for EduNest, a platform connecting schools and students/parents.")
                        .termsOfService("https://EduNest.com/terms")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
