package org.pokeherb.deliveryservice.domain.command;

import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryStatusUpdateCommand(
        UUID deliveryId,
        DeliveryStatus newStatus,
        UUID deliveryDriverId,
        LocalDateTime changedAt
) {
}