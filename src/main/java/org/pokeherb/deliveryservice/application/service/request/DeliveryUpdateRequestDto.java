package org.pokeherb.deliveryservice.application.service.request;

import org.pokeherb.deliveryservice.domain.command.DeliveryUpdateCommand;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DeliveryUpdateRequestDto(
        UUID orderId,
        String receiverName,
        UUID receiverSlackId,
        String endVendorAddress,
        List<Long> sequence,
        Long startHubId,
        Long endHubId,
        UUID endVendorId,
        Double  expectedDurationMin,
        Double  expectedDistanceKm,
        UUID productId,
        LocalDateTime dueAt,
        UUID orderUserId,
        String productName,
        UUID driverId
) {
    public DeliveryUpdateCommand toCommand(){
        return new DeliveryUpdateCommand(
                 sequence,
                 startHubId,
                 endHubId,
                 endVendorId,
                 endVendorAddress,
                 receiverSlackId,
                 receiverName,
                 expectedDurationMin,
                 expectedDistanceKm,
                 productId,
                 dueAt,
                 orderUserId,
                 productName,
                 driverId
        );
    }
}
