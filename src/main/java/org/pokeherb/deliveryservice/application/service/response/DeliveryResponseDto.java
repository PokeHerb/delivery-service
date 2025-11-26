package org.pokeherb.deliveryservice.application.service.response;

import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryResponseDto(
        UUID deliveryId,
        UUID orderId,
        DeliveryStatus deliveryStatus,
        String receiverName,
        Double actualDurationMin,
        Double actualDurationKm,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DeliveryResponseDto from(Delivery d) {
        return new DeliveryResponseDto(
                d.getId(),
                d.getOrderId(),
                d.getDeliveryStatus(),
                d.getReceiverName(),
                d.getActualDurationMin(),
                d.getActualDurationKm(),
                d.getCreatedAt(),
                d.getUpdatedAt()
        );
    }
}