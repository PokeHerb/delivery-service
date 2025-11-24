package org.pokeherb.deliveryservice.application.service.request;

import org.pokeherb.deliveryservice.domain.command.DeliveryCreateCommand;

import java.util.List;
import java.util.UUID;

public record DeliveryCreateRequestDto(
        UUID orderId,
        List<Long> sequence,
        Long startHubId,
        Long endHubId,
        UUID endVendorId,
        String endVendorAddress,
        UUID receiverSlackId,
        String receiverName
) {
    public DeliveryCreateCommand toCommand() {
        return new DeliveryCreateCommand(
                orderId,
                sequence,
                startHubId,
                endHubId,
                endVendorId,
                endVendorAddress,
                receiverSlackId,
                receiverName
        );
    }
}
