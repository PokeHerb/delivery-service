package org.pokeherb.deliveryservice.application.service.request;

import org.pokeherb.deliveryservice.domain.command.RouteStatusChangeCommand;
import org.pokeherb.deliveryservice.domain.entity.RouteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryRouteStatusUpdateRequestDto(
        RouteStatus newStatus,
        LocalDateTime changedAt
) {
    public RouteStatusChangeCommand toCommand(Long routeId, UUID deliveryId) {
        return new RouteStatusChangeCommand(
                routeId,
                deliveryId,
                newStatus,
                changedAt != null ? changedAt : LocalDateTime.now()
        );
    }
}