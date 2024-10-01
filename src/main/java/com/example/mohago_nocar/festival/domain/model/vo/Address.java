package com.example.mohago_nocar.festival.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

    private String roadNameAddress;

    private String jibunAddress;

    private String postalCode;

    // 시/도
    private String city;

    // 구/군/구역
    private String district;

    public static Address from(String roadNameAddress, String jibunAddress, String postalCode, String city, String district) {
        return Address.builder()
                .roadNameAddress(roadNameAddress)
                .jibunAddress(jibunAddress)
                .postalCode(postalCode)
                .city(city)
                .district(district)
                .build();
    }
}
