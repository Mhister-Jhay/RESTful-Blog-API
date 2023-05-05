package com.blogSecurity.authApp.domain.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
@Configuration
public class SwaggerConfig {
    @Value("${application.version}")
    private String version;

    @Bean
    public OpenAPI api(){
        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("jwt", jwtSecurityScheme))
                .info(new Info()
                        .title("Blog App Documentation")
                        .description("Blog App API documentation")
                        .version(version))
                .security(Collections.singletonList(new SecurityRequirement().addList("jwt")));
    }
    @Bean
    public GroupedOpenApi postsEndpoint(){
        return  GroupedOpenApi
                .builder()
                .group("Posts")
                .pathsToMatch("/api/v1/posts/post/**").build();
    }

    @Bean
    public GroupedOpenApi commentEndpoint(){
        return  GroupedOpenApi
                .builder()
                .group("Comments")
                .pathsToMatch("/api/v1/posts/comments/**").build();
    }
    @Bean
    public GroupedOpenApi likeEndpoint(){
        return  GroupedOpenApi
                .builder()
                .group("Likes")
                .pathsToMatch("/api/v1/posts/like/**").build();
    }
    @Bean
    public GroupedOpenApi imageEndpoint(){
        return  GroupedOpenApi
                .builder()
                .group("Image")
                .pathsToMatch("/api/v1/posts/images/**").build();
    }
    @Bean
    public GroupedOpenApi authEndpoint(){
        return  GroupedOpenApi
                .builder()
                .group("Authentication")
                .pathsToMatch("/api/v1/auth/**").build();
    }

}
