package org.pokeherb.deliveryservice.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.deliveryservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RabbitErrorCode implements BaseErrorCode {
    RABBIT_JSON_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RABBIT_JSON_500", "RabbitMQ 메시지를 JSON으로 변환하는 데 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}