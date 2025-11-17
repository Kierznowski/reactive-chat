package com.example.history_service.DTO;


import com.example.common.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MessageEntity {

    @Id
    private String id;
    private MessageType type;
    private String roomId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
}
