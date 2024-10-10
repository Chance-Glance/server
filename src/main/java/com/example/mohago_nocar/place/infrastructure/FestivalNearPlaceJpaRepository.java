package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestivalNearPlaceJpaRepository extends JpaRepository<FestivalNearPlace, Long> {

    Optional<FestivalNearPlace> findByGooglePlaceId(String googlePlaceId);
}
