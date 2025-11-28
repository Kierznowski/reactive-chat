package com.example.auth_server.DTO;

import java.util.Set;
import java.util.UUID;

public record UserAuthDTO(
        UUID id,
        String username,
        String email,
        String passwordHash,
        Set<String> roles
)
{}
