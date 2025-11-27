package com.example.gateway.controller;

import com.example.common.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/gateway/history")
public class HistoryController {

    private final WebClient webClient;

    public HistoryController(@Qualifier("historyWebClient") WebClient historyServiceWebClient) {
        this.webClient = historyServiceWebClient;
    }

    @GetMapping("/{roomId}")
    public Flux<ChatMessage> getRoomHistory(@PathVariable("roomId") String roomId,
                                            @AuthenticationPrincipal OAuth2User principal,
                                            @RegisteredOAuth2AuthorizedClient("chat_auth_server")OAuth2AuthorizedClient client) {
        return webClient.get()
                .uri("http://localhost:9200/history/" + roomId)
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("History error: " + body))))
                .bodyToFlux(ChatMessage.class)
                .onErrorResume(e -> {
                    log.error("Error during history retrieve: {}", e.getMessage());
                    return Flux.empty();
                });
    }
}
