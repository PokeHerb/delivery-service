package org.pokeherb.deliveryservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.domain.command.RouteStatusChangeCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteStatusChangeProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${messaging.exchange.hub-route}")
    private String hubRouteExchange;

    @Value("${messaging.routing-keys.route-status-change}")
    private String routeStatusChangeRoutingKey;

    public void publish(RouteStatusChangeCommand command) {
        // 필요 시 직렬화용 DTO 따로 두고 변환해도 됨
        rabbitTemplate.convertAndSend(hubRouteExchange, routeStatusChangeRoutingKey, command);
    }
}