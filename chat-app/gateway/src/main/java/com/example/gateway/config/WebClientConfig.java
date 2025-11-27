package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean
    @Qualifier("authWebClient")
    public WebClient authorizationServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8000")
                .build();
    }

    @Bean
    @Qualifier("userWebClient")
    public WebClient userServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:9300")
                .build();
    }

    @Bean
    @Qualifier("historyWebClient")
    public WebClient historyServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:9200")
                .build();
    }
}
