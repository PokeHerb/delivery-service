package org.pokeherb.deliveryservice.domain.repository;

import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Delivery, UUID>, JpaSpecificationExecutor<Delivery>, DeliveryRepository {
}