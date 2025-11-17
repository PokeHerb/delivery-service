package org.pokeherb.deliveryservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("example-service")
public interface ExampleClient {
}
