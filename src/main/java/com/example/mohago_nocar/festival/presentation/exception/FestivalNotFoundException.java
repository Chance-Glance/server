package com.example.mohago_nocar.festival.presentation.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.GlobalStatus;

import static com.example.mohago_nocar.festival.presentation.exception.FestivalExceptionCode.FESTIVAL_NOT_FOUND;

public class FestivalNotFoundException extends CustomException {

    public FestivalNotFoundException() {
        super(FESTIVAL_NOT_FOUND);
    }
}
