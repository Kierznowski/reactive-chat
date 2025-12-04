package com.example.history_service.controller;

import com.example.common.model.ChatMessage;
import com.example.history_service.DTO.MessageDTO;
import com.example.history_service.mapper.MessageMapper;
import com.example.history_service.repository.HistoryRepository;
import com.example.history_service.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;


    @GetMapping("/{roomId}")
    public Flux<MessageDTO> getRoomHistory(@PathVariable("roomId") String roomId) {
        return historyService.getRoomHistory(roomId);
    }
}
