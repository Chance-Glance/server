package com.example.mohago_nocar.transit.domain.service;

import com.example.mohago_nocar.transit.domain.model.TransitInfo;

public interface TransitUseCase {

    TransitInfo findTransitInfo(double startX, double startY, double endX, double endY);
}
