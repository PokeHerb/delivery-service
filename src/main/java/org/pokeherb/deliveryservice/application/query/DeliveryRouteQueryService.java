package org.pokeherb.deliveryservice.application.query;

import org.pokeherb.deliveryservice.application.service.response.DeliveryRouteResponseDto;

import java.util.List;
import java.util.UUID;

public interface DeliveryRouteQueryService {

    List<DeliveryRouteResponseDto> getRoutes(UUID deliveryId);

    DeliveryRouteResponseDto getRoute(UUID deliveryId, Long routeId);
}