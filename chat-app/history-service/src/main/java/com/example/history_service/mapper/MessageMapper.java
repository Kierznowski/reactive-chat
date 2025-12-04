package com.example.history_service.mapper;

import com.example.history_service.DTO.MessageHistoryResponse;
import com.example.history_service.DTO.PersistMessageEvent;
import com.example.history_service.model.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageMapper {

    public MessageHistoryResponse fromEntity(Message entity) {
        return new MessageHistoryResponse(
                entity.getId().toString(),
                entity.getType(),
                entity.getRoomId().toString(),
                entity.getSenderId().toString(),
                entity.getContent(),
                entity.getCreatedAt()
        );
    }

    public Message toEntity(PersistMessageEvent event) {
        return new Message(
                UUID.fromString(event.id()),
                event.type(),
                UUID.fromString(event.roomId()),
                UUID.fromString(event.senderId()),
                event.content(),
                event.createdAt()
        );
    }
}
