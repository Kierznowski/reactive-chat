package com.example.user_service.DTO;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String email,
        String username
) {
}
