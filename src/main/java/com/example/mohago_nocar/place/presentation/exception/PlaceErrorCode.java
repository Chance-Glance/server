package com.example.mohago_nocar.place.presentation.exception;

import com.example.mohago_nocar.global.common.exception.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlaceErrorCode implements Status {

    GOOGLE_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND,"PLACE404", "장소를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE404", "장소를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
