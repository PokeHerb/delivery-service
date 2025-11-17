package org.pokeherb.deliveryservice.global.infrastructure.exception;

import lombok.Getter;
import org.pokeherb.deliveryservice.global.infrastructure.error.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException{

    // 예외에서 발생한 에러의 상세 내용
    private final BaseErrorCode code;

    public CustomException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
