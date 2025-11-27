package com.example.gateway.service;

import com.example.gateway.DTO.RegisterRequest;
import com.example.gateway.DTO.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthService {

    private final WebClient webClient;

    public AuthService(@Qualifier("authWebClient") WebClient authorizationServiceWebClient) {
        this.webClient = authorizationServiceWebClient;
    }

    public Mono<RegisterResponse> registerAccount(RegisterRequest request) {
        return webClient.post()
                .uri("/auth/register")
                .bodyValue(new RegisterRequest(
                        request.email(),
                        request.password(),
                        request.username()
                ))
                .exchangeToMono(response -> {
                    if(response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class)
                                .map(body -> new RegisterResponse(true, body));
                    } else {
                        return Mono.error(
                                new RuntimeException("Registration failure: " + response.statusCode())
                        );
                    }
                })
                .doOnSuccess(res -> log.info("User registered"))
                .doOnError(err -> log.error("Registration failed"));
    }
}
