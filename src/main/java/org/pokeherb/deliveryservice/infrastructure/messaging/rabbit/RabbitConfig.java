package org.pokeherb.deliveryservice.infrastructure.messaging.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitDeliveryProperties.class)
public class RabbitConfig {
    private final RabbitDeliveryProperties deliveryProperties;

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(deliveryProperties.exchange(), true, false);
    }

    @Bean
    public Queue deliveryQueue() {
        return QueueBuilder.durable(deliveryProperties.queue()).build();
    }

    @Bean
    public Binding deliveryBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange)
                .with(deliveryProperties.routingKey());
    }
}