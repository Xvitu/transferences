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

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // Main Queue
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE).build();
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
}
