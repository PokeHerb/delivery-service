package org.pokeherb.deliveryservice.application.service.response;

import org.pokeherb.deliveryservice.domain.entity.DeliveryRouteReadModel;
import org.pokeherb.deliveryservice.domain.entity.RouteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryRouteResponseDto(
        Long routeId,
        UUID deliveryId,
        Long hubId,
        RouteStatus routeStatus,
        Integer sortOrder,
        LocalDateTime updatedAt
) {
    public static DeliveryRouteResponseDto from(DeliveryRouteReadModel route) {
        return new DeliveryRouteResponseDto(
                route.getRouteId(),
                route.getDeliveryId(),
                route.getHubId(),
                route.getRouteStatus(),
                route.getSortOrder(),
                route.getUpdatedAt()
        );
    }
}