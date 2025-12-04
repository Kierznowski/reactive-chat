package com.example.gateway.DTO;

import java.time.Instant;
import java.util.UUID;

public record MessageResponseDTO(
    String id,
    String roomId,
    String senderId,
    String senderUsername,
    String content,
    Instant createdAt
)
{}
