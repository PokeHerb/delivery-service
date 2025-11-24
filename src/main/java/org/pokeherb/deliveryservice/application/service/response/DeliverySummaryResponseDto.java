package org.pokeherb.deliveryservice.application.service.response;

import org.pokeherb.deliveryservice.domain.entity.Delivery;

import java.util.UUID;

public record DeliverySummaryResponseDto(
        UUID deliveryId,
        UUID orderId,
        String receiverName
) {

    public static DeliverySummaryResponseDto from(Delivery d) {
        return new DeliverySummaryResponseDto(
                d.getId(),
                d.getOrderId(),
                d.getReceiverName()
        );
    }
}