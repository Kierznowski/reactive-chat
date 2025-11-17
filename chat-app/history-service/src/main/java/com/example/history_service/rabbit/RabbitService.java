package com.example.history_service.rabbit;

import com.example.common.model.ChatMessage;
import com.example.history_service.DTO.MessageEntity;
import com.example.history_service.DTO.MessageMapper;
import com.example.history_service.history.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;

import java.io.IOException;

@Slf4j
@Service
public class RabbitService {

    public final Receiver receiver;
    public final MessageRepository repository;
    public final ObjectMapper objectMapper;
    public final MessageMapper mapper;

    public RabbitService(ConnectionFactory connectionFactory,
                         MessageRepository messageRepository,
                         ObjectMapper objectMapper,
                         MessageMapper messageMapper) {
        this.receiver = RabbitFlux.createReceiver(new ReceiverOptions().connectionFactory(connectionFactory));
        this.objectMapper = objectMapper;
        this.repository = messageRepository;
        this.mapper = messageMapper;
    }

    @PostConstruct
    public void listenForMessageToPersist() {
        receiver.consumeAutoAck("persist.message")
                .map(delivery -> {
                    try {
                        return objectMapper.readValue(delivery.getBody(), ChatMessage.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(this::persistMessage)
                .subscribe(
                        success -> log.info("Message persisted in DB: {}", success.getId()),
                        error -> log.error("Persisting message into DB failure: {}", error.getMessage())
                );
    }

    public Mono<MessageEntity> persistMessage(ChatMessage message) {
        return repository.save(mapper.toEntity(message));
    }
}
