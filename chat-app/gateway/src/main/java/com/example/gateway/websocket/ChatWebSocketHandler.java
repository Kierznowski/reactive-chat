package com.example.gateway.websocket;

import com.example.common.model.ChatMessage;
import com.example.gateway.rabbit.RabbitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    public ChatWebSocketHandler(RabbitService rabbitService,
                                SessionRegistry sessionRegistry,
                                ObjectMapper objectMapper) {
        this.rabbitService = rabbitService;
        this.sessionRegistry = sessionRegistry;
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

    private Mono<ChatMessage> safeParse(String json) {
        try {
            return Mono.just(objectMapper.readValue(json, ChatMessage.class));
        } catch (Exception e) {
            return Mono.empty();
        }
    }

    private Mono<Void> handleIncomingMessage(WebSocketSession session, ChatMessage message) {
        return switch (message.getType()) {
            case MESSAGE -> rabbitService.sendMessage(message);
            case JOIN -> {
                sessionRegistry.addUser(message.getSenderId(), session);
                sessionRegistry.joinRoom(message.getRoomId(), session);
                yield Mono.empty();
            }
            default -> Mono.empty();
        };
    }

    private Mono<Void> sendToRoom(ChatMessage message) {
        Set<WebSocketSession> sessions = sessionRegistry.getSessionsInRoom(message.getRoomId());
        if(sessions == null || sessions.isEmpty()) return Mono.empty();

        return Flux.fromIterable(sessions)
                .flatMap(s -> {
                    try {
                        String json = objectMapper.writeValueAsString(message);
                        return s.send(Mono.just(s.textMessage(json)))
                                .onErrorResume(e -> Mono.empty());
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                }).then();
    }
}
