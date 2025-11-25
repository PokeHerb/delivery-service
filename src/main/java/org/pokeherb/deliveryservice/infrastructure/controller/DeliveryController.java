package org.pokeherb.deliveryservice.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.command.DeliveryCommandService;
import org.pokeherb.deliveryservice.application.query.DeliveryQueryService;
import org.pokeherb.deliveryservice.application.query.DeliveryRouteQueryService;
import org.pokeherb.deliveryservice.application.service.request.DeliveryRouteStatusUpdateRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryRouteResponseDto;
import org.pokeherb.deliveryservice.global.infrastructure.CustomResponse;
import org.pokeherb.deliveryservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryCommandService deliveryCommandService;
    private final DeliveryQueryService deliveryQueryService;
    private final DeliveryRouteQueryService deliveryRouteQueryService;


    /** 배송 경로 목록 조회: GET /api/v1/delivery/{deliveryId}/routes */
    @GetMapping("/{deliveryId}/routes")
    public CustomResponse<List<DeliveryRouteResponseDto>> getRoutes(@PathVariable UUID deliveryId) {
        var routes = deliveryRouteQueryService.getRoutes(deliveryId);
        return CustomResponse.onSuccess(routes);
    }

    /** 배송 경로 단일 상세 조회: GET /api/v1/delivery/{deliveryId}/routes/{routeId} */
    @GetMapping("/{deliveryId}/routes/{routeId}")
    public CustomResponse<DeliveryRouteResponseDto> getRoute(
            @PathVariable UUID deliveryId,
            @PathVariable Long routeId
    ) {
        var route = deliveryRouteQueryService.getRoute(deliveryId, routeId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, route);
    }

    /** 배송 경로 상태 변경: PATCH /api/v1/delivery/{deliveryId}/routes/{routeId}/status */
    @PatchMapping("/{deliveryId}/routes/{routeId}/status")
    public CustomResponse<Void> changeRouteStatus(
            @PathVariable UUID deliveryId,
            @PathVariable Long routeId,
            @RequestBody DeliveryRouteStatusUpdateRequestDto request
    ) {
        deliveryCommandService.changeRouteStatus(deliveryId, routeId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
