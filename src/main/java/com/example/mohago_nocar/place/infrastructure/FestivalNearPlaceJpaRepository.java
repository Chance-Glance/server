package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalNearPlaceJpaRepository extends JpaRepository<FestivalNearPlace, Long> {

    @Query("SELECT place.name FROM FestivalNearPlace place " +
            "WHERE place.location.latitude = :latitude " +
            "AND place.location.longitude = :longitude")
    Page<String> findNamesByLocation(@Param("latitude") Double latitude,
                                     @Param("longitude") Double longitude,
                                     Pageable pageable);

}
