package org.pokeherb.deliveryservice.application.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.service.response.DeliveryRouteResponseDto;
import org.pokeherb.deliveryservice.domain.entity.DeliveryRouteReadModel;
import org.pokeherb.deliveryservice.domain.exception.DeliveryRouteErrorCode;
import org.pokeherb.deliveryservice.domain.repository.DeliveryRouteReadModelRepository;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryRouteQueryServiceImpl implements DeliveryRouteQueryService {

    private final DeliveryRouteReadModelRepository routeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryRouteResponseDto> getRoutes(UUID deliveryId) {
        List<DeliveryRouteReadModel> routes =
                routeRepository.findByDeliveryIdOrderBySortOrderAsc(deliveryId);

        return routes.stream()
                .map(DeliveryRouteResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryRouteResponseDto getRoute(UUID deliveryId, Long routeId) {
        DeliveryRouteReadModel route = routeRepository
                .findByRouteIdAndDeliveryId(routeId, deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryRouteErrorCode.ROUTE_NOT_FOUND));

        return DeliveryRouteResponseDto.from(route);
    }
}