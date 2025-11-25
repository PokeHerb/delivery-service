package org.pokeherb.deliveryservice.application.service.request;

import org.pokeherb.deliveryservice.domain.command.DeliveryUpdateCommand;

import java.util.UUID;

public record DeliveryUpdateRequestDto(
        String receiverName,
        UUID receiverSlackId,
        String endVendorAddress
) {
    public DeliveryUpdateCommand toCommand(UUID deliveryId){
        return new DeliveryUpdateCommand(
            deliveryId,
            receiverName,
            receiverSlackId,
            endVendorAddress
        );
    }
}
