package org.pokeherb.deliveryservice.global.infrastructure.error;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getStatus();
    String getCode();
    String getMessage();

}
