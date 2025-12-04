package com.example.history_service.service;

import com.example.history_service.mapper.MessageMapper;
import com.example.history_service.model.Message;
import com.example.history_service.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MessageMapper mapper;

    public Flux<Message> getRoomHistory(UUID roomId) {
        return historyRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId)
                .doOnNext(msg -> log.info("Retrieved message: {}", msg))
                .doOnError(err -> log.error("Retrieving history error:", err));
    }


    public Mono<Message> persistMessage(Message message) {
        return historyRepository.save(message)
                .doOnSuccess(msg -> log.info("Message {} persisted", msg.getId()))
                .doOnError(err -> log.error("Persisting message failure:", err));
    }
}
