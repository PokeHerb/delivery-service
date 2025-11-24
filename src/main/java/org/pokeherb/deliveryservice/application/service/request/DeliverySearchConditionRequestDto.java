package org.pokeherb.deliveryservice.application.service.request;


import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;

import java.util.UUID;

public record DeliverySearchConditionRequestDto(
        UUID deliveryId,
        UUID orderId,
        DeliveryStatus status,
        String receiverName,
        UUID receiverSlackId,
        UUID deliveryDriverId
) {
}