package org.pokeherb.deliveryservice.infrastructure.messaging.event;

import org.pokeherb.deliveryservice.domain.entity.RouteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteSyncedEvent(
        Long routeId,
        UUID deliveryId,
        Long hubId,
        RouteStatus routeStatus,
        Integer sortOrder,
        LocalDateTime updatedAt
) {}