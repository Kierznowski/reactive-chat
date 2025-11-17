package com.example.history_service.history;

import com.example.history_service.DTO.MessageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, String> {
    Flux<MessageEntity> findByRoomIdOrderByTimestampAsc(String roomId);
}
