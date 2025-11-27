package org.pokeherb.deliveryservice.messaging;

import org.junit.jupiter.api.Test;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCompleteRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCreateRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryStatusUpdateMessageDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryUpdateRequestDto;
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
                orderId
        );
        rabbitProducer.publishDeliveryEvent(dto, "delivery.create");
    }
    @Test
    void sendUpdateDeliveryEvent() throws Exception {
        UUID orderId = UUID.fromString("29431801-472b-4184-93c4-a4c6a00a7aaa");

        DeliveryUpdateRequestDto dto = new DeliveryUpdateRequestDto(
                orderId,
                "홍길동",
                UUID.randomUUID(),
                "서울특별시 xxx",
                List.of(1L, 2L, 3L),
                10L,
                20L,
                UUID.randomUUID(),
                30.5,
                12.3,
                UUID.randomUUID(),
                LocalDateTime.now().plusHours(2),
                UUID.randomUUID(),
                "상품 A",
                UUID.randomUUID()
        );

        rabbitProducer.publishDeliveryEvent(dto, "delivery.update");
    }


    @Test
    void sendRealDeliveryStatusEvent() throws Exception {
        UUID orderId = UUID.fromString("29431801-472b-4184-93c4-a4c6a00a7aaa");

        DeliveryStatusUpdateMessageDto dto = new DeliveryStatusUpdateMessageDto(
                orderId,
                DeliveryStatus.ASSIGNED,
                LocalDateTime.now()
        );

        rabbitProducer.publishDeliveryEvent(dto, "delivery.status");
    }

    @Test
    void completeDeliveryStatusEvent() throws Exception {
        UUID deliveryId = UUID.fromString("b96866ac-ee16-4ba8-adc4-846df8d15cb0");
        Double actualDurationMin = 300.0;
        Double actualDurationKm  = 100.0;

        DeliveryCompleteRequestDto dto = new DeliveryCompleteRequestDto(
                deliveryId,
                actualDurationMin,
                actualDurationKm
        );
        rabbitProducer.publishDeliveryEvent(dto, "delivery.complete");

    }
}