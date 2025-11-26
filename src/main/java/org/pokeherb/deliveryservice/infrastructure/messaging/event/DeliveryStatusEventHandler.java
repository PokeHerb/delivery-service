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
        DeliveryStatusUpdateMessageDto event =
                readPayload(payload, DeliveryStatusUpdateMessageDto.class);

        try {
            deliveryCommandService.updateStatus(event.deliveryId(), event);
        } catch (CustomException e) {
            log.warn("배송 상태 업데이트 실패 - 존재하지 않는 배송: {}", event.deliveryId(), e);
            return; // 예외 안 던지면 Rabbit 측에서는 정상 처리로 보고 ACK
        }

        log.info("Delivery status updated via MQ event: deliveryId={}, newStatus={}",
                event.deliveryId(), event.newStatus());
    }
}
