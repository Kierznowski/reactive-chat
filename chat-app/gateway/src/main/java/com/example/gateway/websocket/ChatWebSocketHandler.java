package com.example.gateway.websocket;

import com.example.common.model.ChatMessage;
import com.example.common.model.MessageType;
import com.example.gateway.rabbit.RabbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public ChatWebSocketHandler(RabbitService rabbitService, SessionRegistry sessionRegistry, ObjectMapper objectMapper) {
        this.rabbitService = rabbitService;
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(text -> {
                    try {
                       return objectMapper.readValue(text, ChatMessage.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorContinue((error, errMsg) -> log.warn("Invalid message: {}", errMsg))
                .flatMap(chatMessage -> {
                    if(chatMessage.getType().equals(MessageType.MESSAGE)) {
                        return rabbitService.sendMessage(chatMessage);
                    } else if(chatMessage.getType().equals(MessageType.JOIN)) {
                        sessionRegistry.addUser(chatMessage.getSenderId(), session);
                        sessionRegistry.joinRoom(chatMessage.getRoomId(), session);
                        System.out.println("User registered: " + chatMessage.getSenderId());
                        return Mono.empty();
                    }
                    return Mono.empty();
                })
                .then();

        Mono<Void> output = rabbitService.listenForMessageToResend()
                .flatMap(msg -> {
                    Set<WebSocketSession> sessions = sessionRegistry.getSessionsInRoom(msg.getRoomId());
                    if (sessions.isEmpty()) return Mono.empty();
                    return Flux.fromIterable(sessions)
                            .flatMap(s -> {
                                try {
                                    String json = objectMapper.writeValueAsString(msg);
                                    return s.send(Mono.just(s.textMessage(json)));
                                } catch (JsonProcessingException e) {
                                    return Mono.error(e);
                                }
                            }).then();
                }).then();

        return input.and(output);
    }

}
