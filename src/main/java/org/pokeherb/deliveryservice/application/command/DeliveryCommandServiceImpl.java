package org.pokeherb.deliveryservice.application.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.service.request.DeliveryCreateRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryRouteStatusUpdateRequestDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryStatusUpdateMessageDto;
import org.pokeherb.deliveryservice.application.service.request.DeliveryUpdateRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliveryCreateResponseDto;
import org.pokeherb.deliveryservice.domain.command.DeliveryCreateCommand;
import org.pokeherb.deliveryservice.domain.command.DeliveryStatusUpdateCommand;
import org.pokeherb.deliveryservice.domain.command.DeliveryUpdateCommand;
import org.pokeherb.deliveryservice.domain.command.RouteStatusChangeCommand;
import org.pokeherb.deliveryservice.domain.entity.Delivery;
import org.pokeherb.deliveryservice.domain.exception.DeliveryErrorCode;
import org.pokeherb.deliveryservice.domain.repository.DeliveryRepository;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.pokeherb.deliveryservice.infrastructure.messaging.RouteStatusChangeProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryCommandServiceImpl implements DeliveryCommandService {

    private final DeliveryRepository deliveryRepository;
    private final RouteStatusChangeProducer routeStatusChangeProducer;

    @Transactional
    public DeliveryCreateResponseDto createDelivery(DeliveryCreateRequestDto requestDto){
        DeliveryCreateCommand command = requestDto.toCommand();
        Delivery saved = deliveryRepository.save(Delivery.create(command));
        return new DeliveryCreateResponseDto(saved.getId());
    }

    @Transactional
    public void updateStatus(UUID deliveryId, DeliveryStatusUpdateMessageDto requestDto){
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        DeliveryStatusUpdateCommand command = requestDto.toCommand(deliveryId);
        delivery.applyStatusUpdate(command);
    }

    @Transactional
    public void updateDelivery(UUID deliveryId, DeliveryUpdateRequestDto requestDto){
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        DeliveryUpdateCommand command = requestDto.toCommand(deliveryId);
        delivery.update(command);
    }

    @Transactional
    public void completeDelivery(UUID deliveryId, Integer actualDurationMin, Integer actualDurationKm) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        delivery.complete(actualDurationMin, actualDurationKm);
    }

    @Transactional
    public void deleteDelivery(UUID deliveryId, String username){
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        delivery.delete(username);
    }

    @Transactional
    public void changeRouteStatus(UUID deliveryId, Long routeId, DeliveryRouteStatusUpdateRequestDto requestDto) {
        RouteStatusChangeCommand command = requestDto.toCommand(routeId, deliveryId);
        routeStatusChangeProducer.publish(command);
    }
}
