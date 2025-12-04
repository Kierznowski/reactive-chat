package com.example.history_service.DTO;

import com.example.common.model.MessageType;

import java.time.Instant;

public record PersistMessageEvent(
    String id,
    MessageType type,
    String roomId,
    String senderId,
    String content,
    Instant createdAt
){}
