package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> {})
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/gateway/auth/status").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(o -> o.authenticationSuccessHandler(successHandler()));

        return http.build();
    }

    @Bean
    public ServerAuthenticationSuccessHandler successHandler() {
        return (webFilterExchange, authentication) ->
                Mono.fromRunnable(() ->
                                webFilterExchange.getExchange()
                                        .getResponse()
                                        .setStatusCode(HttpStatus.FOUND))
                        .then(
                                Mono.fromRunnable(() ->
                                        webFilterExchange.getExchange()
                                                .getResponse()
                                                .getHeaders()
                                                .setLocation(URI.create("http://localhost:3000")))
                        );

    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
