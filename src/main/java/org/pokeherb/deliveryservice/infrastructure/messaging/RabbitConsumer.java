package org.pokeherb.deliveryservice.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class RabbitConsumer {

    @RabbitListener(bindings = @QueueBinding(exchange = @Exchange("pokeherb"), value=@Queue("delivery"), key="test.#"))
    public void handleMessage(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Received message: {}, Routing Key: {}, payload: {}", message, routingKey, payload);
    }
}