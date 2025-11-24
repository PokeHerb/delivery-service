package org.pokeherb.deliveryservice.application.query;

import org.pokeherb.deliveryservice.application.service.request.DeliverySearchConditionRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryResponseDto;
import org.pokeherb.deliveryservice.application.service.response.DeliverySummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeliveryQueryService {

    DeliveryResponseDto getDelivery(UUID deliveryId);

    Page<DeliverySummaryResponseDto> searchDeliveries(DeliverySearchConditionRequestDto condition, Pageable pageable);

    Page<DeliverySummaryResponseDto> getMyDeliveries(UUID driverId, Pageable pageable);
}