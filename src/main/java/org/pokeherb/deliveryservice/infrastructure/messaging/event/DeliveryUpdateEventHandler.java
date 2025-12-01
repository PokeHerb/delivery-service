package org.pokeherb.deliveryservice.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryUpdateRequestDto;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.pokeherb.deliveryservice.infrastructure.messaging.messageHandler.AbstractDeliveryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("delivery.update")
public class DeliveryUpdateEventHandler extends AbstractDeliveryEventHandler {

    private final DeliveryCommandService deliveryCommandService;

    public DeliveryUpdateEventHandler(
            com.fasterxml.jackson.databind.ObjectMapper objectMapper,
            DeliveryCommandService deliveryCommandService
    ) {
        super(objectMapper);
        this.deliveryCommandService = deliveryCommandService;
    }

    public void handle(String payload) {
        try {
            // 공통 메서드로 JSON → DTO 변환
            DeliveryUpdateRequestDto dto =
                    readPayload(payload, DeliveryUpdateRequestDto.class);
            // 도메인 서비스 호출
            deliveryCommandService.updateDelivery(dto);
            log.info("Delivery created via MQ event: orderId={}", dto.orderId());
        } catch (CustomException e) {
            log.error("Error processing DeliveryCreatedHandler", e);
        } catch (Exception e) {
            log.error("Unexpected error while processing order create MQ. payload={}", payload, e);
        }
    }
}