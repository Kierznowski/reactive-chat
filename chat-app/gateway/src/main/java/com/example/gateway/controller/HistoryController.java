package com.example.gateway.controller;

import com.example.gateway.DTO.MessageResponseDTO;
import com.example.gateway.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/gateway/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{roomId}")
    public Flux<MessageResponseDTO> getRoomHistory(@PathVariable("roomId") String roomId,
                                                   @AuthenticationPrincipal OAuth2User principal,
                                                   @RegisteredOAuth2AuthorizedClient("chat_auth_server")OAuth2AuthorizedClient client) {
        return historyService.getRoomHistory(roomId, principal, client);
    }
}
