package org.pokeherb.deliveryservice.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.query.DeliveryQueryService;
import org.pokeherb.deliveryservice.application.service.request.*;
import org.pokeherb.deliveryservice.application.service.response.DeliveryCreateResponseDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryResponseDto;
import org.pokeherb.deliveryservice.application.service.response.DeliverySummaryResponseDto;
import org.pokeherb.deliveryservice.global.infrastructure.CustomResponse;
import org.pokeherb.deliveryservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryCommandService deliveryCommandService;
    private final DeliveryQueryService deliveryQueryService;

    /* ============================================================
       1. 배송 생성
     ============================================================ */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER','COMPANY_MANAGER')")
    public CustomResponse<DeliveryCreateResponseDto> createDelivery(
            @RequestBody @Valid DeliveryCreateRequestDto request
    ) {
        DeliveryCreateResponseDto response = deliveryCommandService.createDelivery(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    /* ============================================================
       2. 배송 상세 조회
     ============================================================ */
    @GetMapping("/{deliveryId}")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<DeliveryResponseDto> getDelivery(
            @PathVariable UUID deliveryId
    ) {
        DeliveryResponseDto dto = deliveryQueryService.getDelivery(deliveryId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, dto);
    }

    /* ============================================================
       3. 배송 목록 조회 (검색 + 페이지네이션)
     ============================================================ */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Page<DeliverySummaryResponseDto>> searchDeliveries(
            @ModelAttribute DeliverySearchConditionRequestDto condition,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<DeliverySummaryResponseDto> page =
                deliveryQueryService.searchDeliveries(condition, pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, page);
    }

    /* ============================================================
       4. 내 배송 목록 조회 (driverId 기준)
     ============================================================ */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Page<DeliverySummaryResponseDto>> getMyDeliveries(
            @RequestParam UUID driverId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<DeliverySummaryResponseDto> page =
                deliveryQueryService.getMyDeliveries(driverId, pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, page);
    }

    /* ============================================================
       5. 배송 수정 (부분 업데이트)
     ============================================================ */
    @PatchMapping("/{deliveryId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    public CustomResponse<Void> updateDelivery(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid DeliveryUpdateRequestDto request
    ) {
        deliveryCommandService.updateDelivery(deliveryId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    /* ============================================================
       6. 배송 상태 변경
     ============================================================ */
    @PatchMapping("/{deliveryId}/status")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    public CustomResponse<Void> updateDeliveryStatus(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid DeliveryStatusUpdateMessageDto request
    ) {
        deliveryCommandService.updateStatus(deliveryId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    /* ============================================================
       7. 배송 완료 처리 (실제 소요 시간/거리 기록)
     ============================================================ */
    @PatchMapping("/{deliveryId}/complete")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    public CustomResponse<Void> completeDelivery(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid DeliveryCompleteRequestDto request
    ) {
        deliveryCommandService.completeDelivery(
                deliveryId,
                request.actualDurationMin(),
                request.actualDurationKm()
        );
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    /* ============================================================
       8. 배송 삭제 (soft delete)
     ============================================================ */
    @DeleteMapping("/{deliveryId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    public CustomResponse<Void> deleteDelivery(
            @PathVariable UUID deliveryId,
            @RequestHeader("X-User-Name") String username
    ) {
        deliveryCommandService.deleteDelivery(deliveryId, username);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    /* ============================================================
       9. 배송 경로 상태 변경 (hub_route 서비스로 이벤트 발행)
     ============================================================ */
    @PatchMapping("/{deliveryId}/routes/{routeId}/status")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    public CustomResponse<Void> changeRouteStatus(
            @PathVariable UUID deliveryId,
            @PathVariable Long routeId,
            @RequestBody @Valid DeliveryRouteStatusUpdateRequestDto request
    ) {
        deliveryCommandService.changeRouteStatus(deliveryId, routeId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
