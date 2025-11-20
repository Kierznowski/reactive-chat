package com.example.history_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class ProjectConfig {

    @Value("${keySetURI}")
    private String keySetUri;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http.oauth2ResourceServer(c -> c.jwt(
                j -> j.jwkSetUri(keySetUri)
        ));

        http.authorizeExchange(
                c -> c.anyExchange().authenticated()
        );

        return http.build();
    }

}
