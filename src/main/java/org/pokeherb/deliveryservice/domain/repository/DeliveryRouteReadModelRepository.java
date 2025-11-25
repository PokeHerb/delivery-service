package org.pokeherb.deliveryservice.domain.repository;

import org.pokeherb.deliveryservice.domain.entity.DeliveryRouteReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteReadModelRepository extends JpaRepository<DeliveryRouteReadModel, Long> {

    List<DeliveryRouteReadModel> findByDeliveryIdOrderBySortOrderAsc(UUID deliveryId);

    Optional<DeliveryRouteReadModel> findByRouteIdAndDeliveryId(Long routeId, UUID deliveryId);
}