package org.pokeherb.deliveryservice.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCreateRequestDto;
import org.pokeherb.deliveryservice.infrastructure.messaging.messageHandler.AbstractDeliveryEventHandler;
import org.springframework.stereotype.Component;

;

@Slf4j
@Component("delivery.created")
public class DeliveryCreatedHandler extends AbstractDeliveryEventHandler {

    private final DeliveryCommandService deliveryCommandService;

    public DeliveryCreatedHandler(
            com.fasterxml.jackson.databind.ObjectMapper objectMapper,
            DeliveryCommandService deliveryCommandService
    ) {
        super(objectMapper);
        this.deliveryCommandService = deliveryCommandService;
    }

    public void handle(String payload) {
        // 공통 메서드로 JSON → DTO 변환
        DeliveryCreateRequestDto dto =
                readPayload(payload, DeliveryCreateRequestDto.class);

        // 도메인 서비스 호출
        deliveryCommandService.createDelivery(dto);

        log.info("Delivery created via MQ event: orderId={}", dto.orderId());
    }
}