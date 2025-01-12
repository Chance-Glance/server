package com.example.mohago_nocar.place.infrastructure;

import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FestivalNearPlaceJpaRepository extends JpaRepository<FestivalNearPlace, Long> {

    Page<FestivalNearPlace> findAllByFestivalId(Long festivalId, Pageable pageable);

    List<FestivalNearPlace> findAllByFestivalId(Long festivalId);

}
