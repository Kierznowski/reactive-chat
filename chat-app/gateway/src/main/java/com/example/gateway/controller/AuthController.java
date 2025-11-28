package com.example.gateway.controller;

import com.example.gateway.DTO.RegisterRequest;
import com.example.gateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gateway/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/status")
    public Map<String, Object> getAuthStatus(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return Map.of("authenticated", false);
        }

        return Map.of(
                "authenticated", true,
                "username", user.getName()
            );
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> registerAccount(@RequestBody RegisterRequest request) {

        log.info("Email: {}, password: {}, username: {}", request.email(), request.password(), request.username());
        return authService.registerAccount(request)
                .map(response -> ResponseEntity.ok(Map.of(
                        "status", true,
                        "message", response.message()
                )));

    }
}
