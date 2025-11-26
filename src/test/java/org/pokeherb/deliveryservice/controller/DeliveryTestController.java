package org.pokeherb.deliveryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.query.DeliveryQueryService;
import org.pokeherb.deliveryservice.application.service.request.*;
import org.pokeherb.deliveryservice.application.service.response.DeliveryCreateResponseDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryResponseDto;
import org.pokeherb.deliveryservice.application.service.response.DeliverySummaryResponseDto;
import org.pokeherb.deliveryservice.domain.entity.DeliveryStatus;
import org.pokeherb.deliveryservice.global.infrastructure.success.GeneralSuccessCode;
import org.pokeherb.deliveryservice.infrastructure.controller.DeliveryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DeliveryCommandService deliveryCommandService;

    @MockitoBean
    private DeliveryQueryService deliveryQueryService;

    /* ============================================================
       1. 배송 생성
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 생성 성공")
    void createDelivery_success() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID endVendorId = UUID.randomUUID();
        UUID receiverSlackId = UUID.randomUUID();

        DeliveryCreateRequestDto request = new DeliveryCreateRequestDto(
                orderId,
                List.of(1L, 2L, 3L),
                100L,
                200L,
                endVendorId,
                "테스트 주소",
                receiverSlackId,
                "테스트 수령인"
        );

        DeliveryCreateResponseDto response = org.mockito.Mockito.mock(DeliveryCreateResponseDto.class);
        given(deliveryCommandService.createDelivery(any(DeliveryCreateRequestDto.class))).willReturn(response);

        mockMvc.perform(post("/api/v1/delivery")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());

        verify(deliveryCommandService).createDelivery(any(DeliveryCreateRequestDto.class));
    }

    /* ============================================================
       2. 배송 수정
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 수정 성공")
    void updateDelivery_success() throws Exception {
        UUID deliveryId = UUID.randomUUID();

        DeliveryUpdateRequestDto request = new DeliveryUpdateRequestDto(
                "수정된 수령인",
                UUID.randomUUID(),
                "수정된 주소"
        );

        mockMvc.perform(patch("/api/v1/delivery/{deliveryId}", deliveryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()));

        verify(deliveryCommandService).updateDelivery(eq(deliveryId), any(DeliveryUpdateRequestDto.class));
    }

    /* ============================================================
       3. 배송 상태 변경
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 상태 변경 성공")
    void updateStatus_success() throws Exception {
        UUID deliveryId = UUID.randomUUID();

        DeliveryStatusUpdateMessageDto request =
                new DeliveryStatusUpdateMessageDto(
                        DeliveryStatus.ASSIGNED,
                        UUID.randomUUID(),
                        LocalDateTime.now()
                );

        mockMvc.perform(patch("/api/v1/delivery/{deliveryId}/status", deliveryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()));

        verify(deliveryCommandService).updateStatus(eq(deliveryId), any(DeliveryStatusUpdateMessageDto.class));
    }

    /* ============================================================
       4. 배송 완료 처리
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 완료 처리 성공")
    void completeDelivery_success() throws Exception {
        UUID deliveryId = UUID.randomUUID();

        DeliveryCompleteRequestDto request =
                new DeliveryCompleteRequestDto(
                        30,
                        12
                );

        mockMvc.perform(patch("/api/v1/delivery/{deliveryId}/complete", deliveryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()));

        verify(deliveryCommandService).completeDelivery(
                eq(deliveryId),
                eq(request.actualDurationMin()),
                eq(request.actualDurationKm())
        );
    }

    /* ============================================================
       5. 배송 삭제
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 삭제 성공")
    void deleteDelivery_success() throws Exception {
        UUID deliveryId = UUID.randomUUID();
        String username = "test-user";

        mockMvc.perform(delete("/api/v1/delivery/{deliveryId}", deliveryId)
                        .with(csrf())
                        .header("X-User-Name", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()));

        verify(deliveryCommandService).deleteDelivery(eq(deliveryId), eq(username));
    }

    /* ============================================================
       6. 배송 상세 조회
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 상세 조회 성공")
    void getDelivery_success() throws Exception {
        UUID deliveryId = UUID.randomUUID();

        DeliveryResponseDto response = org.mockito.Mockito.mock(DeliveryResponseDto.class);
        given(deliveryQueryService.getDelivery(eq(deliveryId))).willReturn(response);

        mockMvc.perform(get("/api/v1/delivery/{deliveryId}", deliveryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());

        verify(deliveryQueryService).getDelivery(eq(deliveryId));
    }

    /* ============================================================
       7. 배송 목록 검색 + 페이징
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 배송 목록 검색 + 페이징 조회 성공")
    void searchDeliveries_success() throws Exception {
        UUID orderId = UUID.randomUUID();

        DeliverySummaryResponseDto dto1 = org.mockito.Mockito.mock(DeliverySummaryResponseDto.class);
        DeliverySummaryResponseDto dto2 = org.mockito.Mockito.mock(DeliverySummaryResponseDto.class);

        Page<DeliverySummaryResponseDto> page =
                new PageImpl<>(List.of(dto1, dto2), PageRequest.of(0, 10), 2);

        given(deliveryQueryService.searchDeliveries(any(DeliverySearchConditionRequestDto.class), any(Pageable.class)))
                .willReturn(page);

        mockMvc.perform(get("/api/v1/delivery")
                        .with(csrf())
                        .param("orderId", orderId.toString())
                        .param("receiverName", "테스트")
                        .param("status", DeliveryStatus.CREATED.name())
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").value(2));

        verify(deliveryQueryService)
                .searchDeliveries(any(DeliverySearchConditionRequestDto.class), any(Pageable.class));
    }

    /* ============================================================
       8. 내 배송 목록 조회 (getMyDeliveries)
     ============================================================ */
    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 내 배송 목록 조회 성공")
    void getMyDeliveries_success() throws Exception {
        UUID driverId = UUID.randomUUID();

        DeliverySummaryResponseDto dto1 = org.mockito.Mockito.mock(DeliverySummaryResponseDto.class);
        DeliverySummaryResponseDto dto2 = org.mockito.Mockito.mock(DeliverySummaryResponseDto.class);

        Page<DeliverySummaryResponseDto> page =
                new PageImpl<>(List.of(dto1, dto2), PageRequest.of(0, 10), 2);

        given(deliveryQueryService.getMyDeliveries(eq(driverId), any(Pageable.class)))
                .willReturn(page);

        mockMvc.perform(get("/api/v1/delivery/my")
                        .with(csrf())
                        .param("driverId", driverId.toString())
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").value(2));

        verify(deliveryQueryService).getMyDeliveries(eq(driverId), any(Pageable.class));
    }

    /* ============================================================
       9. 배송 경로 상태 변경
     ============================================================ */
//    @Test
//    @WithMockUser
//    @DisplayName("컨트롤러: 배송 경로 상태 변경 성공")
//    void changeRouteStatus_success() throws Exception {
//        UUID deliveryId = UUID.randomUUID();
//        Long routeId = 1L;
//
//        DeliveryRouteStatusUpdateRequestDto request =
//                new DeliveryRouteStatusUpdateRequestDto(
//                        null // 실제 필드 (RouteStatus 등) 에 맞게 채우면 됨
//                );
//
//        mockMvc.perform(patch("/api/v1/delivery/{deliveryId}/routes/{routeId}/status", deliveryId, routeId)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.isSuccess").value(true))
//                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()));
//
//        verify(deliveryCommandService)
//                .changeRouteStatus(eq(deliveryId), eq(routeId), any(DeliveryRouteStatusUpdateRequestDto.class));
//    }
}
