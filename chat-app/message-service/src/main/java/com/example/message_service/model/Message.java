package com.example.message_service.model;

import com.example.common.model.MessageType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Message {
    private String id;
    private MessageType type;
    private Long roomId;
    private UUID senderId;
    private String content;
    private Instant createdAt;

    public void setCreatedAt() {
        createdAt = Instant.now();
    }
}
