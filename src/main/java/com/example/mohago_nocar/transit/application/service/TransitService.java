package com.example.mohago_nocar.transit.application.service;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.application.mapper.TransitMapper;
import com.example.mohago_nocar.transit.domain.model.TransitInfo;
import com.example.mohago_nocar.transit.domain.service.TransitUseCase;
import com.example.mohago_nocar.transit.infrastructure.externalApi.ODsayApiClient;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.response.RouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransitService implements TransitUseCase {

    private final ODsayApiClient oDsayApiClient;

    @Override
    public TransitInfo findRouteTransitBetweenPlaces(Location from, Location to) {
        RouteResponseDto response = oDsayApiClient.searchRoute(
                from.getLongitude(),
                from.getLatitude(),
                to.getLongitude(),
                to.getLatitude()
        );

        return TransitMapper.mapRouteResponseDtoToTransitInfo(response);
    }
}
