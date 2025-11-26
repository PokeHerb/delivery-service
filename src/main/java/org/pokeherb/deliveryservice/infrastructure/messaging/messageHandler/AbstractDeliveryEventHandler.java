package org.pokeherb.deliveryservice.infrastructure.messaging.messageHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.pokeherb.deliveryservice.infrastructure.exception.RabbitErrorCode;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDeliveryEventHandler implements DeliveryEventHandler {

    protected final ObjectMapper objectMapper;

    protected <T> T readPayload(String payload, Class<T> type) {
        try {
            return objectMapper.readValue(payload, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse event payload: {}", payload, e);
            throw new CustomException(RabbitErrorCode.RABBIT_JSON_PROCESSING_FAILED);
        }
    }
}