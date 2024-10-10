package com.example.mohago_nocar.transit.domain.service;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.domain.model.TransitInfo;

public interface TransitUseCase {

    TransitInfo findRouteTransitBetweenPlaces(Location from, Location to);
}
