package org.pokeherb.deliveryservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(org.pokeherb.orderservice.infrastructure.config.RabbitProductProperties.class)
public class RabbitConfig {
    private final org.pokeherb.orderservice.infrastructure.config.RabbitProductProperties productProperties;

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(productProperties.getExchange(), true, false);
    }

    @Bean
    public Queue productQueue() {
        return QueueBuilder.durable(productProperties.getQueue()).build();
    }

    @Bean
    public Binding productBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange)
                .with(productProperties.getRoutingKey());
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
