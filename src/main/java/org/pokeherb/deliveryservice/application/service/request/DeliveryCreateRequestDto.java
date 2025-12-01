package org.pokeherb.deliveryservice.application.service.request;

import org.pokeherb.deliveryservice.domain.command.DeliveryCreateCommand;

import java.util.UUID;

public record DeliveryCreateRequestDto(
        UUID orderId
) {
    public DeliveryCreateCommand toCommand() {
        return new DeliveryCreateCommand(
                orderId
        );
    }
}
