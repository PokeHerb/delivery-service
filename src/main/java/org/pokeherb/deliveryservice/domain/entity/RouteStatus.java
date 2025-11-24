package org.pokeherb.deliveryservice.domain.entity;

public enum RouteStatus {
    CREATED,
    ARRIVED,
    DEPARTED,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(RouteStatus target) {
        if (this == target) return true;

        return switch (this) {
            case CREATED -> target == ARRIVED || target == CANCELLED;
            case ARRIVED -> target == DEPARTED || target == CANCELLED;
            case DEPARTED -> target == COMPLETED || target == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}