package com.example.history_service.repository;

import com.example.history_service.model.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface HistoryRepository extends ReactiveCrudRepository<Message, String> {
    Flux<Message> findAllByRoomIdOrderByCreatedAtAsc(UUID roomId);
}
