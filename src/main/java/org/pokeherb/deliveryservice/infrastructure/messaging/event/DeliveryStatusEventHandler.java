package org.pokeherb.deliveryservice.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryStatusUpdateMessageDto;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.pokeherb.deliveryservice.infrastructure.messaging.messageHandler.AbstractDeliveryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("delivery.status")
public class DeliveryStatusEventHandler extends AbstractDeliveryEventHandler {

    private final DeliveryCommandService deliveryCommandService;

    public DeliveryStatusEventHandler(
            com.fasterxml.jackson.databind.ObjectMapper objectMapper,
            DeliveryCommandService deliveryCommandService
    ) {
        super(objectMapper);
        this.deliveryCommandService = deliveryCommandService;
    }

    public void handle(String payload) {
        try {
            DeliveryStatusUpdateMessageDto event =
                    readPayload(payload, DeliveryStatusUpdateMessageDto.class);
            deliveryCommandService.updateStatus(event);
            log.info("Delivery status updated via MQ event: orderId={}, newStatus={}",
                    event.orderId(), event.newStatus());
        } catch (CustomException e) {
            log.error("Error processing DeliveryStatusEventHandler", e);
        } catch (Exception e) {
            log.error("Unexpected error while processing order create MQ. payload={}", payload, e);
        }
    }
}
