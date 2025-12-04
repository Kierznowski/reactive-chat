package com.example.gateway.DTO;

import com.example.common.model.MessageType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@Data
@RequiredArgsConstructor
public class OutcomingMessageEvent {
    private String id;
    private MessageType type;
    private String roomId;
    private String senderId;
    private String content;
    private Instant createdAt;
}
