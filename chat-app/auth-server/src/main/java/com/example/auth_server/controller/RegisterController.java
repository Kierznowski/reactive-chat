package com.example.auth_server.controller;

import com.example.auth_server.DTO.RegisterRequest;
import com.example.auth_server.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {


    private final WebClient webClient;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        UserDTO result = webClient.post()
                .uri("http://localhost:9400/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

        return ResponseEntity.ok(result);
    }
}
