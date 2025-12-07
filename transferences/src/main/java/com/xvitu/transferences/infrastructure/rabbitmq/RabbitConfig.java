package com.xvitu.transferences.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MAIN_EXCHANGE = "transference.exchange";

    public static final String TRANSFERENCE_QUEUE = "transference.queue";
    public static final String TRANSFERENCE_ROUTING_KEY = "transference.key";

    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.key";

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // Transference Queue
    @Bean
    public Queue transferenceQueue() {
        return QueueBuilder.durable(TRANSFERENCE_QUEUE).build();
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    @Bean
    public Binding transferenceBinding() {
        return BindingBuilder.bind(transferenceQueue())
                .to(mainExchange())
                .with(TRANSFERENCE_ROUTING_KEY);
    }

    // Notification queue
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(mainExchange()) // usando o mesmo exchange
                .with(NOTIFICATION_ROUTING_KEY);
    }
}
