package com.example.mohago_nocar.global.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private GlobalStatus globalStatus;

    public CustomException(String message, GlobalStatus errorStatus) {
        super(message);
        this.globalStatus = errorStatus;
    }

    public CustomException(GlobalStatus errorStatus) {
        super(errorStatus.getMessage());
        this.globalStatus = errorStatus;
    }

    public Body getBody() {
        return this.globalStatus.getBody();
    }
}