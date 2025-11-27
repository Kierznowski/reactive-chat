package com.example.gateway.DTO;

import org.springframework.http.HttpStatus;

public record RegisterResponse(
        boolean success,
        String message
)
{ }
