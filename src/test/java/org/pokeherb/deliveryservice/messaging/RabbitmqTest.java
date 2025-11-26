package org.pokeherb.deliveryservice.messaging;

import org.junit.jupiter.api.Test;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCompleteRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCreateRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryStatusUpdateMessageDto;
import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;
import org.pokeherb.deliveryservice.domain.repository.DeliveryRepository;
import org.pokeherb.deliveryservice.infrastructure.messaging.rabbit.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class RabbitmqTest {

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private DeliveryRepository deliveryRepository;
    @MockitoBean
    private DeliveryCommandService deliveryCommandService;

    @Test
    void deliveryCreatedEvent_shouldCreateDelivery() throws Exception {

        // given
        UUID orderId = UUID.randomUUID();

        DeliveryCreateRequestDto dto = new DeliveryCreateRequestDto(
                orderId,
                List.of(1L, 2L),
                1L,
                2L,
                UUID.randomUUID(),
                "테스트 주소",
                UUID.randomUUID(),
                "홍길동",
                10,
                5
        );
        rabbitProducer.publishDeliveryEvent(dto, "delivery.created");
    }

    @Test
    void sendRealDeliveryStatusEvent() throws Exception {
        UUID deliveryId = UUID.fromString("b96866ac-ee16-4ba8-adc4-846df8d15cb0");

        DeliveryStatusUpdateMessageDto dto = new DeliveryStatusUpdateMessageDto(
                deliveryId,
                DeliveryStatus.IN_DELIVERY,
                UUID.randomUUID(),
                LocalDateTime.now()
        );

        rabbitProducer.publishDeliveryEvent(dto, "delivery.status");
    }

    @Test
    void completeDeliveryStatusEvent() throws Exception {
        UUID deliveryId = UUID.fromString("b96866ac-ee16-4ba8-adc4-846df8d15cb0");
        Integer actualDurationMin = 300;
        Integer actualDurationKm  = 100;

        DeliveryCompleteRequestDto dto = new DeliveryCompleteRequestDto(
                deliveryId,
                actualDurationMin,
                actualDurationKm
        );
        rabbitProducer.publishDeliveryEvent(dto, "delivery.complete");

    }
}