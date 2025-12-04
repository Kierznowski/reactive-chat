package com.example.gateway.websocket;

import com.example.gateway.DTO.OutcomingMessageEvent;
import com.example.gateway.DTO.MessageResponseDTO;
import com.example.gateway.DTO.IncomingMessageEvent;
import com.example.gateway.rabbit.RabbitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final RabbitService rabbitService;
    private final SessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    public ChatWebSocketHandler(RabbitService rabbitService, SessionRegistry sessionRegistry,
                                ObjectMapper objectMapper,  @Qualifier("userServiceWebClient") WebClient webClient) {
        this.rabbitService = rabbitService;
        this.sessionRegistry = sessionRegistry;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }


    @PostConstruct
    public void init() {
        rabbitService.listenForMessageToResend()
                .flatMap(this::sendToRoom)
                .onErrorContinue((err, obj) -> log.error("RabbitMQ listener error", err))
                .subscribe();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(this::safeParse)
                .flatMap(msg -> handleIncomingMessage(session, msg))
                .then()
                .doFinally(sig -> sessionRegistry.removeSession(session));
    }

    private Mono<IncomingMessageEvent> safeParse(String json) {
        try {
            return Mono.just(objectMapper.readValue(json, IncomingMessageEvent.class));
        } catch (Exception e) {
            return Mono.empty();
        }
    }

    private Mono<Void> handleIncomingMessage(WebSocketSession session, IncomingMessageEvent message) {
        return switch (message.type()) {
            case MESSAGE -> rabbitService.sendMessageToProcess(message);
            case JOIN -> {
                sessionRegistry.addUser(message.senderId(), session);
                sessionRegistry.joinRoom(message.roomId(), session);
                yield Mono.empty();
            }
            default -> Mono.empty();
        };
    }

    private Mono<Void> sendToRoom(OutcomingMessageEvent message) {
        Set<WebSocketSession> sessions = sessionRegistry.getSessionsInRoom(message.getRoomId().toString());
        if(sessions == null || sessions.isEmpty()) return Mono.empty();

        return webClient.get()
                .uri("/user/{userId}/username", message.getSenderId())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(senderUsername -> {
                    MessageResponseDTO messageResponse = new MessageResponseDTO(
                            message.getId(),
                            message.getRoomId(),
                            message.getSenderId(),
                            senderUsername,
                            message.getContent(),
                            message.getCreatedAt()
                    );

                    return Flux.fromIterable(sessions)
                            .flatMap(s -> {
                                try {
                                    String json = objectMapper.writeValueAsString(messageResponse);
                                    return s.send(Mono.just(s.textMessage(json)))
                                            .onErrorResume(e -> {
                                                log.error("Failed to send message to session {}", s.getId(), e);
                                                return Mono.empty();
                                            });
                                } catch (Exception e) {
                                    log.error("Failed to serialize message", e);
                                    return Mono.empty();
                                }
                            })
                            .then();
                })
                .onErrorResume(e -> {
                    log.error("Failed to fetch username for user {}", message.getSenderId());
                    return Mono.empty();
                });
    }
}
