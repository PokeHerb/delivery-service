package org.pokeherb.deliveryservice.domain.repository;

import org.pokeherb.deliveryservice.application.service.request.DeliverySearchConditionRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliverySummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryQueryRepository {
    Page<DeliverySummaryResponseDto> search(DeliverySearchConditionRequestDto condition, Pageable pageable);
}
