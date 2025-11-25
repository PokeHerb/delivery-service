package org.pokeherb.deliveryservice.domain.repository;

import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository{
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(UUID deliveryId);

    Optional<Delivery> findByDeliveryStatus(DeliveryStatus status);
}
