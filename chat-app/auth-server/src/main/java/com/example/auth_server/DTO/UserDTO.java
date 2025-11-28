package com.example.auth_server.DTO;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email
)
{}
