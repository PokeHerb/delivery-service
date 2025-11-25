package org.pokeherb.deliveryservice.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pokeherb.deliveryservice.global.domain.Auditable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_delivery_route")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRouteReadModel extends Auditable {

    @Id
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "route_status", nullable = false, length = 30)
    private RouteStatus routeStatus;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DeliveryRouteReadModel(
            Long routeId,
            UUID deliveryId,
            Long hubId,
            RouteStatus routeStatus,
            Integer sortOrder,
            LocalDateTime updatedAt
    ) {
        this.routeId = routeId;
        this.deliveryId = deliveryId;
        this.hubId = hubId;
        this.routeStatus = routeStatus;
        this.sortOrder = sortOrder;
        this.updatedAt = updatedAt;
    }

    public void sync(RouteStatus routeStatus, Integer sortOrder, LocalDateTime updatedAt) {
        this.routeStatus = routeStatus;
        this.sortOrder = sortOrder;
        this.updatedAt = updatedAt;
    }

    public void updateStatus(RouteStatus newStatus, LocalDateTime updatedAt) {
        this.routeStatus = newStatus;
        this.updatedAt = updatedAt;
    }
}
