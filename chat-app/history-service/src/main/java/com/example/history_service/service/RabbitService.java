package com.example.history_service.service;

import com.example.history_service.DTO.PersistMessageEvent;
import com.example.history_service.mapper.MessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.rabbitmq.Receiver;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
public class RabbitService {

    private final Receiver receiver;
    private final ObjectMapper objectMapper;
    private final HistoryService historyService;
    private final MessageMapper messageMapper;

    private Disposable subscription;

    public RabbitService(HistoryService historyService,
                         ObjectMapper objectMapper,
                         MessageMapper messageMapper,
                         Receiver receiver) {
        this.receiver = receiver;
        this.objectMapper = objectMapper;
        this.messageMapper = messageMapper;
        this.historyService = historyService;

        startConsumer();

    }

    private void startConsumer() {
        subscription = receiver.consumeAutoAck("persist.message")
                .map(delivery -> {
                    try {
                        return objectMapper.readValue(delivery.getBody(), PersistMessageEvent.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(messageMapper::toEntity)
                .flatMap(historyService::persistMessage)
                .doOnNext(message -> System.out.println(message.getId()))
                .doOnError(e -> log.error("Rabbit error: {}", e.getMessage()))
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(1)))
                .subscribe(
                        msg -> log.info("Persisted message {}", msg.getId()),
                        err -> log.error("Error during persist: ", err));
    }


    @PreDestroy
    public void shutdown() {
        if(subscription != null) {
            subscription.dispose();
        }
    }
}
