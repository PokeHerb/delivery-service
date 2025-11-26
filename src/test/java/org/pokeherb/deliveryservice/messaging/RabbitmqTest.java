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
        UUID productId = UUID.randomUUID();
        UUID orderUserId = UUID.randomUUID();
        UUID endVendorId = UUID.randomUUID();
        UUID receiverSlackId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();

        DeliveryCreateRequestDto dto = new DeliveryCreateRequestDto(
                orderId,
                List.of(1L, 2L),             // sequence
                1L,                          // startHubId
                2L,                          // endHubId
                endVendorId,                 // endVendorId
                "테스트 주소",                // endVendorAddress
                receiverSlackId,             // receiverSlackId
                "홍길동",                     // receiverName
                10.0,                        // finalDuration
                5.0,                         // finalDistance
                productId,                   // productId
                LocalDateTime.now().plusDays(3), // dueAt
                orderUserId,                 // orderUserId
                "테스트 상품",                 // productName
                driverId                     // driverId
        );
        rabbitProducer.publishDeliveryEvent(dto, "delivery.create");
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