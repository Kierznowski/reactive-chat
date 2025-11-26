package com.example.history_service.service;

import com.example.common.model.ChatMessage;
import com.example.history_service.model.MessageEntity;
import com.example.history_service.mapper.MessageMapper;
import com.example.history_service.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
public class RabbitService {

    public final Receiver receiver;
    public final MessageRepository repository;
    public final ObjectMapper objectMapper;
    public final MessageMapper mapper;

    private Disposable subscription;

    public RabbitService(MessageRepository messageRepository,
                         ObjectMapper objectMapper,
                         MessageMapper messageMapper,
                         Receiver receiver) {
        this.receiver = receiver;
        this.objectMapper = objectMapper;
        this.repository = messageRepository;
        this.mapper = messageMapper;

        startConsumer();

    }

    private void startConsumer() {
        subscription = receiver.consumeAutoAck("persist.message")
                .map(delivery -> {
                    try {
                        return objectMapper.readValue(delivery.getBody(), ChatMessage.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(this::persistMessage)
                .doOnError(e -> log.error("Rabbit error: {}", e.getMessage()))
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(1)))
                .subscribe(msg -> log.info("Persisted message {}", msg.getId()));
    }

    public Mono<MessageEntity> persistMessage(ChatMessage message) {
        return repository.save(mapper.toEntity(message));
    }

    @PreDestroy
    public void shutdown() {
        if(subscription != null) {
            subscription.dispose();
        }
    }
}
