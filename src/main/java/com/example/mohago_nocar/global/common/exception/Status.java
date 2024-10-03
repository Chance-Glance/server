package com.example.mohago_nocar.global.common.exception;

import org.springframework.http.HttpStatus;

public interface Status {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
