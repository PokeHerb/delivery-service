package org.pokeherb.deliveryservice.application.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.service.request.DeliverySearchConditionRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryResponseDto;
import org.pokeherb.deliveryservice.application.service.response.DeliverySummaryResponseDto;
import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.pokeherb.deliveryservice.domain.exception.DeliveryErrorCode;
import org.pokeherb.deliveryservice.domain.repository.DeliveryQueryRepository;
import org.pokeherb.deliveryservice.domain.repository.DeliveryRepository;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryQueryServiceImpl implements DeliveryQueryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryQueryRepository deliveryQueryRepository;

    @Transactional(readOnly = true)
    public DeliveryResponseDto getDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        return DeliveryResponseDto.from(delivery);
    }

    @Transactional(readOnly = true)
    public Page<DeliverySummaryResponseDto> searchDeliveries(DeliverySearchConditionRequestDto condition, Pageable pageable) {
        return deliveryQueryRepository.search(condition, pageable);
    }

    public Page<DeliverySummaryResponseDto> getMyDeliveries(UUID driverId, Pageable pageable) {
        var condition = new DeliverySearchConditionRequestDto(
                null, null, null, null, null, driverId
        );
        return deliveryQueryRepository.search(condition, pageable);
    }

}
