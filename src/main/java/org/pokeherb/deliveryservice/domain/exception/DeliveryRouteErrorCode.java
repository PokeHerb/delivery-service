package org.pokeherb.deliveryservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.deliveryservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliveryRouteErrorCode implements BaseErrorCode {
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "DELIVERYROUTE400", "배송 경로 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}