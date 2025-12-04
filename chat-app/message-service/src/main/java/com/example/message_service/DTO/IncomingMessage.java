package com.example.message_service.DTO;

import com.example.common.model.MessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class IncomingMessage {
    private MessageType type;
    private Long roomId;
    private UUID senderId;
    private String content;
}
