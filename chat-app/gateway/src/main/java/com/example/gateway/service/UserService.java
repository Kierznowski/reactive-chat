package com.example.gateway.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(@Qualifier("userWebClient") WebClient userWebClient) {
        this.webClient = userWebClient;
    }

    public Mono<String> getUsernameByUserId(String userId) {
        return webClient.get()
                .uri("/user/{userId}/username", userId)
                .retrieve()
                .bodyToMono(String.class);
    }


}
