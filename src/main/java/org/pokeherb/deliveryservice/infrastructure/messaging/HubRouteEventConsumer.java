package org.pokeherb.deliveryservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.domain.entity.DeliveryRouteReadModel;
import org.pokeherb.deliveryservice.domain.repository.DeliveryRouteReadModelRepository;
import org.pokeherb.deliveryservice.infrastructure.messaging.event.HubRouteDeletedEvent;
import org.pokeherb.deliveryservice.infrastructure.messaging.event.HubRouteSyncedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubRouteEventConsumer {

    private final DeliveryRouteReadModelRepository routeRepository;

    @RabbitListener(queues = "${messaging.queues.hub-route-synced}")
    public void handleRouteSynced(HubRouteSyncedEvent event) {
        routeRepository.findById(event.routeId())
                .ifPresentOrElse(
                        route -> {
                            route.sync(event.routeStatus(), event.sortOrder(), event.updatedAt());
                        },
                        () -> {
                            DeliveryRouteReadModel route = new DeliveryRouteReadModel(
                                    event.routeId(),
                                    event.deliveryId(),
                                    event.hubId(),
                                    event.routeStatus(),
                                    event.sortOrder(),
                                    event.updatedAt()
                            );

                            routeRepository.save(route);
                        }
                );
        // flush 는 @Transactional 또는 repo.save(...) 로 처리
    }

    @RabbitListener(queues = "${messaging.queues.hub-route-deleted}")
    public void handleRouteDeleted(HubRouteDeletedEvent event) {
        routeRepository.deleteById(event.routeId());
    }
}