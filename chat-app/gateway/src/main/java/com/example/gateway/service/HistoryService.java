package com.example.gateway.service;

import com.example.gateway.DTO.MessageResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class HistoryService {

    private final WebClient historyWebClient;
    private final UserService userService;

    public HistoryService(@Qualifier("historyWebClient") WebClient historyWebCLient,
                          UserService userService) {
        this.historyWebClient = historyWebCLient;
        this.userService = userService;
    }

    public Flux<MessageResponseDTO> getRoomHistory(
            String roomId,
            @AuthenticationPrincipal OAuth2User principal,
            @RegisteredOAuth2AuthorizedClient("chat_auth_server") OAuth2AuthorizedClient client ) {

        String token = client.getAccessToken().getTokenValue();

        return historyWebClient.get()
                .uri("http://localhost:9200/history/{roomId}", roomId)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(MessageResponseDTO.class)
                .flatMap(message ->
                        userService.getUsernameByUserId(message.senderId())
                                .map(username -> new MessageResponseDTO(
                                        message.id(),
                                        message.roomId(),
                                        message.senderId(),
                                        username,
                                        message.content(),
                                        message.createdAt()
                                ))

                        )
                .onErrorResume(e -> {
                    log.error("Error during history retrieve: {}", e.getMessage());
                    return Flux.empty();
                });
    }
}
