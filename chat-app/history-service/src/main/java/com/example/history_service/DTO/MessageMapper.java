package com.example.history_service.DTO;

import com.example.common.model.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageEntity toEntity(ChatMessage chatMessage) {
        return new MessageEntity(
               chatMessage.getId(),
                chatMessage.getType(),
                chatMessage.getRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getContent(),
                chatMessage.getTimestamp()
        );
    }
}
