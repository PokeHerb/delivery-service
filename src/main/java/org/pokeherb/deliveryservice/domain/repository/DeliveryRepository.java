package org.pokeherb.deliveryservice.domain.repository;

import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository{
    Delivery save(Delivery delivery);

    Optional<Delivery> findByDeliveryId(UUID id);

    Optional<Delivery> findByDeliveryStatus(DeliveryStatus status);
}
