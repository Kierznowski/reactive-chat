package com.example.message_service.DTO;

import com.example.common.model.MessageType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class RegisteredMessageEvent {
    private String id;
    private MessageType type;
    private String roomId;
    private String senderId;
    private String content;
    private Instant createdAt;
}
