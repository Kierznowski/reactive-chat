package com.example.history_service.controller;

import com.example.common.model.ChatMessage;
import com.example.history_service.mapper.MessageMapper;
import com.example.history_service.repository.MessageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final MessageRepository repository;
    private final MessageMapper mapper;

    public HistoryController(MessageRepository repository, MessageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping("/{roomId}")
    public Flux<ChatMessage> getRoomHistory(@PathVariable("roomId") String roomId) {
        return repository.findByRoomIdOrderByTimestampAsc(roomId)
                .map(mapper::fromEntity);
    }
}
