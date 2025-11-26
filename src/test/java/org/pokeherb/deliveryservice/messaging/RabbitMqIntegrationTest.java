package org.pokeherb.deliveryservice.messaging;

//@SpringBootTest
//public class RabbitmqTest {

//    @Autowired
//    private RabbitProducer rabbitProducer;
//    @Autowired
//    private DeliveryRepository deliveryRepository;
//
//    @Test
//    void rabbitmqTest() {
//        DeliveryResponseDto hub = DeliveryResponseDto.from(Objects.requireNonNull(deliveryRepository.findByHubIdAndDeletedAtIsNull(1L).orElse(null)));
//        rabbitProducer.publishDeliveryEvent(hub, "hub.created.order");
//    }
//}