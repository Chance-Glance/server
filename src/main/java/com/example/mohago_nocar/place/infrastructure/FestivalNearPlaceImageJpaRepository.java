package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlaceImage;
import com.example.mohago_nocar.place.domain.repository.FestivalNearPlaceImageRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalNearPlaceImageJpaRepository extends JpaRepository<FestivalNearPlaceImage, Long> {

    List<FestivalNearPlaceImage> findAllByFestivalNearPlaceId(Long id);
}
