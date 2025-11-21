package com.example.history_service.history;

import com.example.common.model.ChatMessage;
import com.example.history_service.DTO.MessageEntity;
import com.example.history_service.DTO.MessageMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = {"http://localhost:9000"})
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
