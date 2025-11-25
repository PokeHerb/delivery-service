package org.pokeherb.deliveryservice.infrastructure.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.hub")
public record RabbitDeliveryProperties(
        String exchange,
        String queue,
        String routingKey
) {
}