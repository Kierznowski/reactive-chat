package com.example.gateway.controller;

import com.example.common.model.ChatMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/gateway/history")
@CrossOrigin(origins = "http://localhost:3000")
public class HistoryController {

    public final WebClient webClient;

    public HistoryController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/{roomId}")
    public Flux<ChatMessage> getRoomHistory(@PathVariable("roomId") String roomId,
                                            @AuthenticationPrincipal OAuth2User principal,
                                            @RegisteredOAuth2AuthorizedClient("chat_auth_server")OAuth2AuthorizedClient client) {
        return WebClient.create()
                .get()
                .uri("http://localhost:9200/history/" + roomId)
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .retrieve()
                .bodyToFlux(ChatMessage.class);
    }
}
