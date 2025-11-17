package org.pokeherb.deliveryservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rabbit.product")
public class RabbitProductProperties {
    private String exchange;
    private String queue;
    private String routingKey;
}
