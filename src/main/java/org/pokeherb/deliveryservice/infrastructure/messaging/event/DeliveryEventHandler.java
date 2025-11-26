package org.pokeherb.deliveryservice.infrastructure.messaging.event;

public interface DeliveryEventHandler {
    void handle(String payload);
}