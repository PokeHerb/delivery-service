package org.pokeherb.deliveryservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.deliveryservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliveryErrorCode implements BaseErrorCode {
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "DELIVERY400_1", "배송 정보를 찾을 수 없습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "DELIVERY400_2", "배송 상태를 해당 상태로 변경할 수 없습니다."),
    CANNOT_COMPLETE_DELIVERY(HttpStatus.BAD_REQUEST, "DELIVERY400_3", "배송을 완료 처리할 수 없는 상태입니다."),
    DELIVERY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "DELIVERY400_4", "이미 삭제된 배송입니다."),
    CANNOT_UPDATE_DELIVERY(HttpStatus.BAD_REQUEST, "DELIVERY400_5", "업데이트가 불가능합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}