package org.pokeherb.deliveryservice.domain.command;

import java.util.UUID;

public record DeliveryCreateCommand(
        UUID orderId
) {
}