package com.example.mohago_nocar.festival.presentation.exception;

import com.example.mohago_nocar.global.common.exception.GlobalStatus;
import com.example.mohago_nocar.global.common.exception.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum FestivalExceptionCode implements Status {
    FESTIVAL_NOT_FOUND(NOT_FOUND, "축제를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}