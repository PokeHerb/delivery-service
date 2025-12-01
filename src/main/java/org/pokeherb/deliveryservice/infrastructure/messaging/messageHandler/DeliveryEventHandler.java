package org.pokeherb.deliveryservice.infrastructure.messaging.messageHandler;

public interface DeliveryEventHandler {
    void handle(String payload);
}