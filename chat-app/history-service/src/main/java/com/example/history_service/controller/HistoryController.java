package com.example.history_service.controller;

import com.example.history_service.DTO.MessageHistoryResponse;
import com.example.history_service.mapper.MessageMapper;
import com.example.history_service.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final MessageMapper mapper;


    @GetMapping("/{roomId}")
    public Flux<MessageHistoryResponse> getRoomHistory(@PathVariable("roomId") String roomId) {
        log.info("Controller called");
        return historyService.getRoomHistory(UUID.fromString(roomId)).map(mapper::fromEntity)
                .doOnNext(msg -> log.info("retrieved: {}", msg));
    }
}
