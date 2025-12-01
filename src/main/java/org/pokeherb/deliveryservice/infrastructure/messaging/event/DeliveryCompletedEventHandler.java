package org.pokeherb.deliveryservice.infrastructure.messaging.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCompleteRequestDto;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.pokeherb.deliveryservice.infrastructure.messaging.messageHandler.AbstractDeliveryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("delivery.complete")
public class DeliveryCompletedEventHandler extends AbstractDeliveryEventHandler {

    private final DeliveryCommandService deliveryCommandService;

    public DeliveryCompletedEventHandler(
            ObjectMapper objectMapper,
            DeliveryCommandService deliveryCommandService
    ) {
        super(objectMapper);
        this.deliveryCommandService = deliveryCommandService;
    }
    public void handle(String payload) {
        try {
            DeliveryCompleteRequestDto event =
                    readPayload(payload, DeliveryCompleteRequestDto.class);

            deliveryCommandService.completeDelivery(
                    event.deliveryId(),
                    event.actualDurationMin(),
                    event.actualDurationKm()
            );
        } catch (CustomException e) {
            log.error("Error processing DeliveryCompletedEventHandler", e);
        } catch (Exception e) {
            log.error("Unexpected error while processing order create MQ. payload={}", payload, e);
        }
    }
}