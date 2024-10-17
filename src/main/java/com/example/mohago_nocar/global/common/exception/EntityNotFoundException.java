package com.example.mohago_nocar.global.common.exception;

public class EntityNotFoundException extends CustomException{

    public EntityNotFoundException() {
        super(GlobalStatus.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(Status status) {
        super(status);
    }

    public EntityNotFoundException(String message, Status status) {
        super(message, status);
    }
}
