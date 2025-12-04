package com.example.gateway.DTO;

import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

public record RoomDTO(
        Long id,
        String name,
        UUID ownerId,
        List<User> memberIds
)
{}

