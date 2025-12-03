package com.example.history_service.service;

import com.example.common.model.ChatMessage;
import com.example.history_service.DTO.MessageDTO;
import com.example.history_service.mapper.MessageMapper;
import com.example.history_service.model.MessageEntity;
import com.example.history_service.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MessageMapper mapper;

    public Flux<MessageDTO> getRoomHistory(String roomId) {
        return historyRepository.findByRoomIdOrderByCreatedAtAsc(roomId).map(mapper::fromEntity);
    }


    public Mono<MessageEntity> persistMessage(MessageEntity message) {
        return historyRepository.save(message);
    }
}
