package com.example.gateway.DTO;

import java.time.Instant;
import java.util.UUID;

public record BroadcastMessage(
    String id,
    Long roomId,
    UUID senderId,
    String senderUsername,
    String content,
    Instant createdAt
)
{}
