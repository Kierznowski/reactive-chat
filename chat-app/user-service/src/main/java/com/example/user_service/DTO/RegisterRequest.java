package com.example.user_service.DTO;

public record RegisterRequest(
        String email, String password, String username
) {}
