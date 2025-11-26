package com.example.history_service.repository;

import com.example.history_service.model.MessageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, String> {
    Flux<MessageEntity> findByRoomIdOrderByTimestampAsc(String roomId);
}
