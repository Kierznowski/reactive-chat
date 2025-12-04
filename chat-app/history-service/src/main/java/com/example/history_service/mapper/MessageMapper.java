package com.example.history_service.mapper;

import com.example.common.model.ChatMessage;
import com.example.history_service.DTO.MessageDTO;
import com.example.history_service.model.MessageEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDTO fromEntity(MessageEntity entity) {
        return new MessageDTO(
                entity.getId(),
                entity.getType(),
                entity.getRoomId(),
                entity.getSenderId(),
                entity.getContent(),
                entity.getCreatedAt()
        );
    }
}
