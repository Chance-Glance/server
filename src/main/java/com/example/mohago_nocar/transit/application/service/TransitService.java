package com.example.mohago_nocar.transit.application.service;

import com.example.mohago_nocar.transit.domain.model.TransitInfo;
import com.example.mohago_nocar.transit.infrastructure.externalApi.ODsayApiClient;
import com.example.mohago_nocar.transit.infrastructure.externalApi.dto.OdsayResponse;
import com.example.mohago_nocar.transit.infrastructure.mapper.TransitPathMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransitService {

    private final ODsayApiClient oDsayApiClient;

    public TransitInfo findTransitInfo(double startX, double startY, double endX, double endY) {
        OdsayResponse response = oDsayApiClient.request(startX, startY, endX, endY);

        return TransitPathMapper.mapToTransitInfo(response);
    }
}
