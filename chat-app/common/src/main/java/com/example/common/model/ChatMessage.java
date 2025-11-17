package com.example.common.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Data
public class ChatMessage {

    private String id;
    private MessageType type;
    private String roomId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;

    public void setTimeStamp() {
        timestamp = LocalDateTime.now();
    }
}
