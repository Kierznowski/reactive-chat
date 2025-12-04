package com.example.auth_server.DTO;

public record RegisterUserRequest(
        String email,
        String password,
        String username) {
}
