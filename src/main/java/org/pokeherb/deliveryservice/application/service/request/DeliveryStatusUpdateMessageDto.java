package org.pokeherb.deliveryservice.application.service.request;

import org.pokeherb.deliveryservice.domain.command.DeliveryStatusUpdateCommand;
import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryStatusUpdateMessageDto(
        UUID orderId,
        DeliveryStatus newStatus,
        LocalDateTime changedAt
) {

    public DeliveryStatusUpdateCommand toCommand(UUID orderId) {
        return new DeliveryStatusUpdateCommand(
                orderId,
                newStatus,
                changedAt
        );
    }
}
