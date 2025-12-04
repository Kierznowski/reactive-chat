package com.example.history_service.DTO;

import com.example.common.model.MessageType;

import java.time.Instant;
import java.util.UUID;

public record MessageDTO(
        String id,
        MessageType type,
        Long roomId,
        UUID senderId,
        String content,
        Instant createdAt
) {}
