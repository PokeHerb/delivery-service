package org.pokeherb.deliveryservice.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.query.DeliveryQueryService;
import org.pokeherb.deliveryservice.application.service.request.*;
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
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryCommandService deliveryCommandService;
    private final DeliveryQueryService deliveryQueryService;


    /* ============================================================
       1. 배송 상세 조회
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
       2. 배송 목록 조회 (검색 + 페이지네이션)
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
       3. 내 배송 목록 조회 (driverId 기준)
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
       4. 배송 수정 (부분 업데이트)
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
       5. 배송 상태 변경
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
       6. 배송 삭제 (soft delete)
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

}
