package com.example.history_service.model;


import com.example.common.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Message {

    @Id
    private UUID id;
    private MessageType type;
    private UUID roomId;
    private UUID senderId;
    private String content;
    private Instant createdAt;
}
