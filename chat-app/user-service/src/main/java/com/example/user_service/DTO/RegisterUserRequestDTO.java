package com.example.user_service.DTO;

public record RegisterUserRequestDTO(
        String email, String password, String username
) {}
