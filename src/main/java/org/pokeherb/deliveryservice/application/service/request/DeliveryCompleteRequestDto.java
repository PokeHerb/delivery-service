package org.pokeherb.deliveryservice.application.service.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 배송 완료 시 실제 소요 시간/거리 기록용 요청 DTO
 */
public record DeliveryCompleteRequestDto(

        UUID deliveryId,

        @NotNull
        @Min(0)
        Double actualDurationMin,   // 실제 소요 시간(분)

        @NotNull
        @Min(0)
        Double actualDurationKm     // 실제 이동 거리(km)
) {
}
