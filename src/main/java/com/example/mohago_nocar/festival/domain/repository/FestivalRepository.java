package com.example.mohago_nocar.festival.domain.repository;

import com.example.mohago_nocar.festival.domain.model.Festival;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalRepository {

    Page<Festival> getFestivals(Pageable pageable);

    Optional<Festival> getFestivalById(Long id);

    List<Festival> getAllFestivals();
}
