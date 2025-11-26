package org.pokeherb.deliveryservice.application.command;

import org.pokeherb.deliveryservice.application.service.request.DeliveryCreateRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryRouteStatusUpdateRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryStatusUpdateMessageDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryUpdateRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryCreateResponseDto;

import java.util.UUID;

public interface DeliveryCommandService {
        DeliveryCreateResponseDto createDelivery(DeliveryCreateRequestDto requestDto);

        void updateStatus(UUID deliveryId, DeliveryStatusUpdateMessageDto requestDto);

        void updateDelivery(UUID deliveryId, DeliveryUpdateRequestDto requestDto);

        void deleteDelivery(UUID deliveryId, String username);

        void completeDelivery(UUID deliveryId, Double actualDurationMin, Double actualDurationKm);

        void changeRouteStatus(UUID deliveryId, Long routeId, DeliveryRouteStatusUpdateRequestDto requestDto);
}
