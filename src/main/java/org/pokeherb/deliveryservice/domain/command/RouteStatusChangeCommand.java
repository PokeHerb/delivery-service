package org.pokeherb.deliveryservice.domain.command;

import org.pokeherb.deliveryservice.domain.entity.RouteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RouteStatusChangeCommand(
        Long routeId,
        UUID deliveryId,
        RouteStatus newStatus,
        LocalDateTime changedAt
) {
}