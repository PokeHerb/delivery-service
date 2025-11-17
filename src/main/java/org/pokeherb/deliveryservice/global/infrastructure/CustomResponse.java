package org.pokeherb.deliveryservice.global.infrastructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.pokeherb.deliveryservice.global.infrastructure.error.BaseErrorCode;
import org.pokeherb.deliveryservice.global.infrastructure.success.BaseSuccessCode;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess","status","code","message","result"})
// 필드값이 null 인 경우 포함 X
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private T result;

    public static CustomResponse<?> onSuccess(BaseSuccessCode baseSuccessCode) {
        return CustomResponse.builder()
                .isSuccess(true)
                .status(baseSuccessCode.getStatus())
                .code(baseSuccessCode.getCode())
                .message(baseSuccessCode.getMessage())
                .build();
    }

    public static <T> CustomResponse<T> onSuccess(BaseSuccessCode baseSuccessCode, T result) {
        return CustomResponse.<T>builder()
                .isSuccess(true)
                .status(baseSuccessCode.getStatus())
                .code(baseSuccessCode.getCode())
                .message(baseSuccessCode.getMessage())
                .result(result)
                .build();
    }

    public static <T> CustomResponse<T> onSuccess(T result) {
        return new CustomResponse<>(true, HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), result);
    }

    // CustomException 이 컨트롤러에서 발생하면, 이 메서드로 예외 처리를 위임
    public static CustomResponse<?> onFail(BaseErrorCode errorCode) {
        return CustomResponse.builder()
                .isSuccess(false)
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> CustomResponse<T> onFail(HttpStatus status, String code, String message, boolean isSuccess, T result) {
        return new CustomResponse<>(isSuccess, status, code, message , result);
    }

}
