package com.example.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProjectConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .filter(ExchangeFilterFunctions.statusError(
                        HttpStatusCode::isError,
                        response -> new RuntimeException("Gateway WebClient error: " + response.statusCode())
                ))
                .build();
    }
}
