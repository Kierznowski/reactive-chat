package com.example.user_service.DTO;

import java.util.Set;

public record UserAuthResponseDTO(
        String id,
        String username,
        String email,
        String passwordHash,
        Set<String> roles
) {
}
