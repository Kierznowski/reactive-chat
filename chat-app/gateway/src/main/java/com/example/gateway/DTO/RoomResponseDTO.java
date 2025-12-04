package com.example.gateway.DTO;

import org.springframework.security.core.userdetails.User;

import java.util.List;

public record RoomResponseDTO(
        String id,
        String name,
        String ownerId,
        List<User> memberIds
)
{}

