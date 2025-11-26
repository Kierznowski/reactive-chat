package com.example.message_service.service;

import com.example.common.model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.io.IOException;
import java.util.UUID;

@Service
public class RabbitService {

    public final Receiver receiver;
    public final Sender sender;
    public final ObjectMapper objectMapper;

    public RabbitService(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        this.receiver = RabbitFlux.createReceiver(new ReceiverOptions().connectionFactory(connectionFactory));
        this.sender = RabbitFlux.createSender(new SenderOptions().connectionFactory(connectionFactory));
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void startListening() {
        receiver.consumeAutoAck("chat.messages")
                .map(delivery -> {
                    try {
                        return objectMapper.readValue(delivery.getBody(), ChatMessage.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(this::registerMessage)
                .flatMap(this::transformToBytes)
                .flatMap(this::sendMessageToChat)
                .flatMap(this::sendMessageToHistory)
                .subscribe();
    }

    Mono<byte[]> transformToBytes(ChatMessage chatMessage) {
        try {
            byte[] msgData = objectMapper.writeValueAsBytes(chatMessage);
            return Mono.just(msgData);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    Mono<byte[]> sendMessageToChat(byte[] chatMessage) {
        OutboundMessage msg = new OutboundMessage(
                "",
                "processed.message",
                chatMessage
        );
        return sender.send(Mono.just(msg)).thenReturn(chatMessage);
    }

    Mono<byte[]> sendMessageToHistory(byte[] chatMessage) {
        OutboundMessage msg = new OutboundMessage(
                "",
                "persist.message",
                chatMessage
            );
            return sender.send(Mono.just(msg)).thenReturn(chatMessage);
    }

    private Mono<ChatMessage> registerMessage(ChatMessage chatMessage) {
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setTimeStamp();
        return Mono.just(chatMessage);
    }
}
