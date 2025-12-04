package com.example.gateway.DTO;

import com.example.common.model.MessageType;

public record IncomingMessageEvent(
        MessageType type,
        String roomId,
        String senderId,
        String content
) {
}
