package org.pokeherb.deliveryservice.infrastructure.messaging.event;

import org.springframework.stereotype.Component;

@Component("delivery.created")
public class DeliveryCreatedHandler implements DeliveryEventHandler {
    @Override
    public void handle(String payload) {
        // created 로직
    }
}