package com.example.mohago_nocar.global.common.response;

import com.example.mohago_nocar.global.common.exception.GlobalStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.onSuccess(GlobalStatus.OK.getCode(), GlobalStatus.OK.getMessage(), data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.onSuccess(GlobalStatus.CREATED.getCode(), GlobalStatus.CREATED.getMessage(), data);
    }

    public static <T> ApiResponse<T> onSuccess(String status, String message, T data) {
        return new ApiResponse<>(true, status, message, data);
    }

    public static <T> ApiResponse<T> onFailure(String status, String message, T data) {
        return  new ApiResponse<>(false, status, message, data);
    }
}
