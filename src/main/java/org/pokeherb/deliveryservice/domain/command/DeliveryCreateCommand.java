package org.pokeherb.deliveryservice.domain.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DeliveryCreateCommand(
        UUID orderId,
        List<Long> sequence,
        Long startHubId,
        Long endHubId,
        UUID endVendorId,
        String endVendorAddress,
        UUID receiverSlackId,
        String receiverName,
        Double  expectedDurationMin,
        Double  expectedDistanceKm,
        UUID productId,
        LocalDateTime dueAt,
        UUID orderUserId,
        String productName,
        UUID driverId
) {
}