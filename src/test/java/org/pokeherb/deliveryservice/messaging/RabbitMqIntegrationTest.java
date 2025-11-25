package org.pokeherb.deliveryservice.messaging;

import org.junit.jupiter.api.Test;
import org.pokeherb.deliveryservice.application.service.response.DeliveryResponseDto;
import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.pokeherb.deliveryservice.domain.repository.DeliveryRepository;
import org.pokeherb.deliveryservice.infrastructure.messaging.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class RabbitmqTest {

    @Autowired
    private RabbitProducer rabbitProducer;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    void rabbitmqTest() {
        DeliveryResponseDto hub = DeliveryResponseDto.from(Objects.requireNonNull(deliveryRepository.findByHubIdAndDeletedAtIsNull(1L).orElse(null)));
        rabbitProducer.publishDeliveryEvent(hub, "hub.created.order");
    }
}