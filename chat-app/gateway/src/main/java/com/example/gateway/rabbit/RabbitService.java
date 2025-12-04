package com.example.gateway.rabbit;

import com.example.common.model.ChatMessage;
import com.example.gateway.DTO.ReceivedMessage;
import com.example.gateway.websocket.SessionRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.io.IOException;

@Service
public class RabbitService {

    public final Receiver receiver;
    public final Sender sender;
    public final ObjectMapper objectMapper;

    private final Flux<ChatMessage> processedMessageStream;


    public RabbitService(ConnectionFactory connectionFactory,
                         SessionRegistry sessionRegistry,
                         ObjectMapper objectMapper,
                         Receiver receiver,
                         Sender sender) {
        this.receiver = receiver;
        this.sender = sender;
        this.objectMapper = objectMapper;

        this.processedMessageStream = receiver.consumeAutoAck("processed.message")
                .map(delivery -> {
                    try {
                        return objectMapper.readValue(delivery.getBody(), ChatMessage.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .share();
    }

    public Flux<ChatMessage> listenForMessageToResend() {
        return processedMessageStream;
    }

    public Mono<Void> sendMessageToProcess(ReceivedMessage receivedMessage) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(receivedMessage);

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
}
