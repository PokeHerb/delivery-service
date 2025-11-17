package org.pokeherb.deliveryservice.global.infrastructure.success;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {

    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
