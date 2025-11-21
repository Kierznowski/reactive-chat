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

    public ChatMessage fromEntity(MessageEntity entity) {
        return new ChatMessage(
            entity.getId(),
                entity.getType(),
                entity.getRoomId(),
                entity.getSenderId(),
                entity.getContent(),
                entity.getTimestamp()
        );
    }
}
