package com.example.history_service.history;

import com.example.history_service.DTO.MessageEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/history")
public class HistoryController {

    private final MessageRepository repository;

    public HistoryController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{roomId}")
    public Flux<MessageEntity> getRoomHistory(@PathVariable("roomId") String roomId) {
        return repository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
