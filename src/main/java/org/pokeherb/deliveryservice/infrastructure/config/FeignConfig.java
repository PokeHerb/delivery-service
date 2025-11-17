package org.pokeherb.deliveryservice.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("org.pokeherb.orderservice")
public class FeignConfig {
}
