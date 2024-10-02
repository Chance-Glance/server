package com.example.mohago_nocar.festival.infrastructure;

import com.example.mohago_nocar.festival.domain.model.FestivalImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalImageJpaRepository extends JpaRepository<FestivalImage, Long> {

    List<FestivalImage> findAllByFestivalId(Long festivalId);
}
