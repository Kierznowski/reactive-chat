package com.example.auth_server.DTO;

public record RegisterRequest(
        String email,
        String password,
        String username) {
}
