package com.example.history_service.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.rabbitmq.*;

@Configuration
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setHost("localhost");
        cf.setUsername("guest");
        cf.setPassword("guest");
        return cf;
    }


    @Bean
    public Receiver receiver() {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionFactory(connectionFactory()));
    }

    @Bean
    public Sender sender() {
        return RabbitFlux.createSender(new SenderOptions().connectionFactory(connectionFactory()));
    }
}
