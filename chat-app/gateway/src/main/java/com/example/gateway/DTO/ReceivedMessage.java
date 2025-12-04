package com.example.gateway.DTO;

import com.example.common.model.MessageType;

public record ReceivedMessage(
        MessageType type,
        Long roomId,
        String senderId,
        String content
) {
}
