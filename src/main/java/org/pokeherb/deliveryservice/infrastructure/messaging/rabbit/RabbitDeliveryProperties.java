package org.pokeherb.deliveryservice.infrastructure.messaging.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.delivery")
public record RabbitDeliveryProperties(
        String exchange,
        String queue,
        String routingKey
) {
}