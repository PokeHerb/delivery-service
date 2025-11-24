package org.pokeherb.deliveryservice.infrastructure.messaging.event;

public record HubRouteDeletedEvent(
        Long routeId
) {}