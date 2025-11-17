package com.example.gateway.rabbit;

import com.example.common.model.ChatMessage;
import com.example.gateway.websocket.SessionRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.io.IOException;

@Service
public class RabbitService {

    public final Receiver receiver;
    public final Sender sender;
    public final SessionRegistry sessionRegistry;
    public final ObjectMapper objectMapper;

    public RabbitService(ConnectionFactory connectionFactory, SessionRegistry sessionRegistry, ObjectMapper objectMapper) {
        this.receiver = RabbitFlux.createReceiver(new ReceiverOptions().connectionFactory(connectionFactory));
        this.sender = RabbitFlux.createSender(new SenderOptions().connectionFactory(connectionFactory));
        this.objectMapper = objectMapper;
        this.sessionRegistry = sessionRegistry;
    }

    public Mono<Void> sendMessage(ChatMessage chatMessage) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(chatMessage);

            OutboundMessage msg = new OutboundMessage(
                    "",
                    "chat.messages",
                    body
            );
            return sender.send(Mono.just(msg));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    @PostConstruct
    public Flux<ChatMessage> listenForMessageToResend() {
        return receiver.consumeAutoAck("processed.message")
                .map(delivery -> {
                    try {
                        return objectMapper.readValue(delivery.getBody(), ChatMessage.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
