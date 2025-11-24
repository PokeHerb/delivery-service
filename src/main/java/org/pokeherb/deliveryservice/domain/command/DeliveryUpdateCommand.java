package org.pokeherb.deliveryservice.domain.command;

import java.util.UUID;

public record DeliveryUpdateCommand(
        UUID deliveryId,
        String receiverName,
        UUID receiverSlackId,
        String endVendorAddress
) {
}