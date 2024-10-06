package com.example.mohago_nocar.place.domain.model;

import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FestivalNearPlaceImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private Long festivalNearPlaceId;

    @NotNull
    private String imageUrl;

    public static FestivalNearPlaceImage from(Long festivalId, String imageUrl) {
        return FestivalNearPlaceImage.builder()
                .festivalNearPlaceId(festivalId)
                .imageUrl(imageUrl)
                .build();
    }

    @Builder
    private FestivalNearPlaceImage(Long festivalNearPlaceId, String imageUrl) {
        this.festivalNearPlaceId = festivalNearPlaceId;
        this.imageUrl = imageUrl;
    }
}
