package com.xvitu.transferences.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MAIN_EXCHANGE = "transference.exchange";
    public static final String MAIN_QUEUE = "transference.queue";
    public static final String MAIN_ROUTING_KEY = "transference.key";

    public static final String DLX_EXCHANGE = "transference.dlq.exchange";
    public static final String DLQ_QUEUE = "transference.queue.dlq";

    public static final String RETRY_QUEUE = "transference.retry.queue";
    public static final String RETRY_ROUTING_KEY = "transference.retry.key";

    // TTL de retry (5 segundos)
    public static final int RETRY_TTL = 5000;

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // MAIN QUEUE
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", MAIN_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(mainQueue())
                .to(mainExchange())
                .with(MAIN_ROUTING_KEY);
    }

    // RETRY QUEUE
    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(RETRY_QUEUE)
                .withArgument("x-message-ttl", RETRY_TTL)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", MAIN_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryQueue())
                .to(mainExchange())
                .with(RETRY_ROUTING_KEY);
    }

    // DLQ
    @Bean
    public Queue dlq() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq())
                .to(dlxExchange())
                .with(MAIN_ROUTING_KEY);
    }
}
