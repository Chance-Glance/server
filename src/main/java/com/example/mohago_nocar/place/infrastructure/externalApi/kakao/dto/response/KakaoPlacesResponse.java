package com.example.mohago_nocar.place.infrastructure.externalApi.kakao.dto.response;

import java.util.List;

public record KakaoPlacesResponse(
        List<KakaoPlaceResponse> documents
) {

    public record KakaoPlaceResponse(
            String x,
            String y,
            String id,
            String place_name,
            String category_group_code,
            String phone,
            String address_name,
            String place_url,
            String distance
    ) {
    }

}
