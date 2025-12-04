package com.example.message_service.service;

import com.example.message_service.DTO.IncomingMessageEvent;
import com.example.message_service.DTO.RegisteredMessageEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.io.IOException;
import java.time.Instant;
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
                        return objectMapper.readValue(delivery.getBody(), IncomingMessageEvent.class);
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

    Mono<byte[]> transformToBytes(RegisteredMessageEvent message) {
        try {
            byte[] msgData = objectMapper.writeValueAsBytes(message);
            return Mono.just(msgData);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    Mono<byte[]> sendMessageToChat(byte[] message) {
        OutboundMessage msg = new OutboundMessage(
                "",
                "processed.message",
                message
        );
        return sender.send(Mono.just(msg)).thenReturn(message);
    }

    Mono<byte[]> sendMessageToHistory(byte[] message) {
        OutboundMessage msg = new OutboundMessage(
                "",
                "persist.message",
                message
            );
            return sender.send(Mono.just(msg)).thenReturn(message);
    }

    private Mono<RegisteredMessageEvent> registerMessage(IncomingMessageEvent message) {
        RegisteredMessageEvent registeredMessage = new RegisteredMessageEvent();
        registeredMessage.setId(UUID.randomUUID().toString());
        registeredMessage.setType(message.type());
        registeredMessage.setRoomId(message.roomId());
        registeredMessage.setSenderId(message.senderId());
        registeredMessage.setContent(message.content());
        registeredMessage.setCreatedAt(Instant.now());

        return Mono.just(registeredMessage);
    }
}
