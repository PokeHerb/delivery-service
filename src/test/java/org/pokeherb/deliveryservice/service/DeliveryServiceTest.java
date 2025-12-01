//package org.pokeherb.deliveryservice.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
//import org.pokeherb.deliveryservice.application.query.DeliveryQueryService;
//import org.pokeherb.deliveryservice.application.service.request.*;
//import org.pokeherb.deliveryservice.application.service.response.*;
//import org.pokeherb.deliveryservice.domain.entity.*;
//import org.pokeherb.deliveryservice.domain.exception.DeliveryErrorCode;
//import org.pokeherb.deliveryservice.domain.repository.DeliveryRepository;
//import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//public class DeliveryServiceTest {
//
//    @Autowired
//    private DeliveryCommandService deliveryCommandService;
//
//    @Autowired
//    private DeliveryQueryService deliveryQueryService;
//
//    @Autowired
//    private DeliveryRepository deliveryRepository;
//
//    private UUID orderId;
//    private Long startHubId;
//    private Long endHubId;
//    private UUID endVendorId;
//    private UUID receiverSlackId;
//    private String receiverName;
//    private String endVendorAddress;
//    private List<Long> sequence;
//    private Integer finalDuration;
//    private Integer finalDistance;
//
//
//    @BeforeEach
//    void init() {
//        orderId = UUID.randomUUID();
//        startHubId = 100L;
//        endHubId = 200L;
//        endVendorId = UUID.randomUUID();
//        receiverSlackId = UUID.randomUUID();
//        receiverName = "테스트 수령인";
//        endVendorAddress = "테스트 주소";
//        sequence = List.of(1L, 2L, 3L);
//        finalDuration = 10;
//        finalDistance = 20;
//    }
//
//    private DeliveryCreateRequestDto createRequest() {
//        return new DeliveryCreateRequestDto(
//                orderId,
//                sequence,
//                startHubId,
//                endHubId,
//                endVendorId,
//                endVendorAddress,
//                receiverSlackId,
//                receiverName,
//                finalDuration,
//                finalDistance
//        );
//    }
//
//    private DeliveryCreateRequestDto createCustom(UUID orderId, String receiverName) {
//        return new DeliveryCreateRequestDto(
//                orderId,
//                sequence,
//                startHubId,
//                endHubId,
//                endVendorId,
//                endVendorAddress,
//                receiverSlackId,
//                receiverName,
//                finalDuration,
//                finalDistance
//        );
//    }
//
//    /* ============================================================
//       1. 배송 생성
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("서비스: 배송 생성")
//    void createDelivery() {
//        DeliveryCreateResponseDto response = deliveryCommandService.createDelivery(createRequest());
//        UUID deliveryId = response.deliveryId();
//
//        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
//
//        assertEquals(orderId, delivery.getOrderId());
//        assertEquals(receiverName, delivery.getReceiverName());
//        assertEquals(endVendorAddress, delivery.getEndVendorAddress());
//        assertEquals(DeliveryStatus.CREATED, delivery.getDeliveryStatus());
//        assertEquals(sequence, delivery.getSequence());
//    }
//
//    /* ============================================================
//       2. 배송 업데이트 (부분 수정)
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("서비스: 배송 수정 (부분 업데이트)")
//    void updateDelivery() {
//        DeliveryCreateResponseDto created = deliveryCommandService.createDelivery(createRequest());
//        UUID deliveryId = created.deliveryId();
//
//        DeliveryUpdateRequestDto updateRequest =
//                new DeliveryUpdateRequestDto(
//                        "(수정)" + receiverName,
//                        receiverSlackId,
//                        "(수정)" + endVendorAddress
//                );
//
//        deliveryCommandService.updateDelivery(deliveryId, updateRequest);
//
//        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
//
//        assertTrue(delivery.getReceiverName().startsWith("(수정)"));
//        assertTrue(delivery.getEndVendorAddress().startsWith("(수정)"));
//        assertEquals(receiverSlackId, delivery.getReceiverSlackId());
//    }
//
//    /* ============================================================
//       3. 배송 삭제
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("서비스: 배송 삭제")
//    void deleteDelivery() {
//        DeliveryCreateResponseDto created = deliveryCommandService.createDelivery(createRequest());
//        UUID deliveryId = created.deliveryId();
//
//        deliveryCommandService.deleteDelivery(deliveryId, "deleter-user");
//
//        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
//        assertNotNull(delivery.getDeletedAt());
//        assertEquals("deleter-user", delivery.getDeletedBy());
//    }
//
//    /* ============================================================
//       4. 배송 상세 조회
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("서비스: 배송 상세 조회")
//    void getDeliveryDetail() {
//        DeliveryCreateResponseDto created = deliveryCommandService.createDelivery(createRequest());
//        UUID deliveryId = created.deliveryId();
//
//        DeliveryResponseDto response = deliveryQueryService.getDelivery(deliveryId);
//
//        assertEquals(deliveryId, response.deliveryId());
//        assertEquals(orderId, response.orderId());
//        assertEquals(receiverName, response.receiverName());
//    }
//
//    /* ============================================================
//       5. 배송 상태 업데이트 (MQ 목적 메시지 기반)
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("상태 변경: CREATED → IN_TRANSIT 성공")
//    void updateStatus_success() {
//        DeliveryCreateResponseDto created = deliveryCommandService.createDelivery(createRequest());
//        UUID deliveryId = created.deliveryId();
//
//        DeliveryStatusUpdateMessageDto aasigned =
//                new DeliveryStatusUpdateMessageDto(
//                        DeliveryStatus.ASSIGNED,
//                        UUID.randomUUID(),
//                        LocalDateTime.now()
//                );
//        deliveryCommandService.updateStatus(deliveryId, aasigned);
//
//        DeliveryStatusUpdateMessageDto pickUp =
//                new DeliveryStatusUpdateMessageDto(
//                        DeliveryStatus.CANCELLED,
//                        UUID.randomUUID(),
//                        LocalDateTime.now()
//                );
//        deliveryCommandService.updateStatus(deliveryId, pickUp);
//
//        UUID driverId = UUID.randomUUID();
//        DeliveryStatusUpdateMessageDto inDelivery =
//                new DeliveryStatusUpdateMessageDto(
//                        DeliveryStatus.IN_DELIVERY,
//                        driverId,
//                        LocalDateTime.now()
//                );
//        deliveryCommandService.updateStatus(deliveryId, inDelivery);
//
//        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
//
//        assertEquals(DeliveryStatus.IN_DELIVERY, delivery.getDeliveryStatus());
//        assertEquals(driverId, delivery.getDeliveryDriverId());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("상태 변경: 잘못된 전이 예외 (CREATED → COMPLETED)")
//    void updateStatus_invalid() {
//        DeliveryCreateResponseDto created = deliveryCommandService.createDelivery(createRequest());
//        UUID deliveryId = created.deliveryId();
//
//        DeliveryStatusUpdateMessageDto message =
//                new DeliveryStatusUpdateMessageDto(
//                        DeliveryStatus.COMPLETED,
//                        null,
//                        LocalDateTime.now()
//                );
//
//        CustomException ex = assertThrows(CustomException.class,
//                () -> deliveryCommandService.updateStatus(deliveryId, message));
//
//        assertEquals(DeliveryErrorCode.INVALID_STATUS_TRANSITION, ex.getCode());
//    }
//
//    /* ============================================================
//       6. 배송 검색
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("배송 검색: receiverName 기준")
//    void searchByReceiverName() {
//
//        deliveryCommandService.createDelivery(createCustom(orderId, "홍길동"));
//        deliveryCommandService.createDelivery(createCustom(orderId, "홍길자"));
//        deliveryCommandService.createDelivery(createCustom(orderId, "김철수"));
//
//        DeliverySearchConditionRequestDto condition =
//                new DeliverySearchConditionRequestDto(
//                        null,
//                        null,
//                        null,
//                        "홍",
//                        null,
//                        null
//                );
//
//        Page<DeliverySummaryResponseDto> page =
//                deliveryQueryService.searchDeliveries(condition, PageRequest.of(0, 10));
//
//        assertEquals(2, page.getTotalElements());
//        assertTrue(page.getContent().stream()
//                .allMatch(d -> d.receiverName().contains("홍")));
//    }
//
//    /* ============================================================
//       7. 배송 검색 + 페이지네이션
//     ============================================================ */
//    @Test
//    @Transactional
//    @DisplayName("배송 검색 (페이지네이션 동작)")
//    void pagination() {
//
//        for (int i = 1; i <= 25; i++) {
//            deliveryCommandService.createDelivery(
//                    createCustom(orderId, "수령자-" + i)
//            );
//        }
//
//        DeliverySearchConditionRequestDto condition =
//                new DeliverySearchConditionRequestDto(
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null
//                );
//
//        Page<DeliverySummaryResponseDto> page0 =
//                deliveryQueryService.searchDeliveries(condition, PageRequest.of(0, 10));
//
//        assertEquals(25, page0.getTotalElements());
//        assertEquals(10, page0.getContent().size());
//        assertEquals(3, page0.getTotalPages());
//
//        Page<DeliverySummaryResponseDto> page2 =
//                deliveryQueryService.searchDeliveries(condition, PageRequest.of(2, 10));
//
//        assertEquals(5, page2.getContent().size());
//        assertEquals(2, page2.getNumber());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("검색: soft delete된 배송은 결과에서 제외되어야 한다.")
//    void search_excludeDeleted(){
//        DeliveryCreateResponseDto created1 = deliveryCommandService.createDelivery(createRequest());
//        DeliveryCreateResponseDto created2 = deliveryCommandService.createDelivery(createRequest());
//
//        UUID deleteTargetId = created1.deliveryId();
//        deliveryCommandService.deleteDelivery(deleteTargetId, "tester");
//
//        DeliverySearchConditionRequestDto condition = new DeliverySearchConditionRequestDto(
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        Page<DeliverySummaryResponseDto> page = deliveryQueryService.searchDeliveries(condition, PageRequest.of(0,10));
//
//        assertEquals(1, page.getTotalElements());
//        assertTrue(page.getContent().stream()
//                .noneMatch(d -> d.receiverName().equals("삭제대상")));
//    }
//    @Test
//    @Transactional
//    @DisplayName("배송 검색: receiverSlackId 기준")
//    void searchByReceiverSlackId() {
//
//        UUID slackA = UUID.randomUUID();
//        UUID slackB = UUID.randomUUID();
//
//        DeliveryCreateRequestDto dtoA1 = new DeliveryCreateRequestDto(
//                orderId,
//                sequence,
//                startHubId,
//                endHubId,
//                endVendorId,
//                endVendorAddress,
//                slackA,
//                "슬랙A-1"
//        );
//        DeliveryCreateRequestDto dtoA2 = new DeliveryCreateRequestDto(
//                orderId,
//                sequence,
//                startHubId,
//                endHubId,
//                endVendorId,
//                endVendorAddress,
//                slackA,
//                "슬랙A-2"
//        );
//        DeliveryCreateRequestDto dtoB = new DeliveryCreateRequestDto(
//                orderId,
//                sequence,
//                startHubId,
//                endHubId,
//                endVendorId,
//                endVendorAddress,
//                slackB,
//                "슬랙B-1"
//        );
//
//        deliveryCommandService.createDelivery(dtoA1);
//        deliveryCommandService.createDelivery(dtoA2);
//        deliveryCommandService.createDelivery(dtoB);
//
//        DeliverySearchConditionRequestDto condition =
//                new DeliverySearchConditionRequestDto(
//                        null,
//                        null,
//                        null,
//                        null,
//                        slackA,
//                        null
//                );
//
//        Page<DeliverySummaryResponseDto> page =
//                deliveryQueryService.searchDeliveries(condition, PageRequest.of(0, 10));
//
//        assertEquals(2, page.getTotalElements());
//        assertTrue(page.getContent().stream()
//                .allMatch(d -> d.receiverSlackId().equals(slackA)));
//    }
//    @Test
//    @Transactional
//    @DisplayName("배송 검색: orderId 기준")
//    void searchByOrderId() {
//
//        UUID orderA = UUID.randomUUID();
//        UUID orderB = UUID.randomUUID();
//
//        deliveryCommandService.createDelivery(
//                new DeliveryCreateRequestDto(
//                        orderA,
//                        sequence,
//                        startHubId,
//                        endHubId,
//                        endVendorId,
//                        endVendorAddress,
//                        receiverSlackId,
//                        "orderA-1"
//                )
//        );
//        deliveryCommandService.createDelivery(
//                new DeliveryCreateRequestDto(
//                        orderA,
//                        sequence,
//                        startHubId,
//                        endHubId,
//                        endVendorId,
//                        endVendorAddress,
//                        receiverSlackId,
//                        "orderA-2"
//                )
//        );
//        deliveryCommandService.createDelivery(
//                new DeliveryCreateRequestDto(
//                        orderB,
//                        sequence,
//                        startHubId,
//                        endHubId,
//                        endVendorId,
//                        endVendorAddress,
//                        receiverSlackId,
//                        "orderB-1"
//                )
//        );
//
//        DeliverySearchConditionRequestDto condition =
//                new DeliverySearchConditionRequestDto(
//                        null,
//                        null,
//                        null,
//                        null,
//                        orderA,
//                        null
//                );
//
//        Page<DeliverySummaryResponseDto> page =
//                deliveryQueryService.searchDeliveries(condition, PageRequest.of(0, 10));
//
//        assertEquals(2, page.getTotalElements());
//        assertTrue(page.getContent().stream()
//                .allMatch(d -> d.orderId().equals(orderA)));
//    }
//
//}
