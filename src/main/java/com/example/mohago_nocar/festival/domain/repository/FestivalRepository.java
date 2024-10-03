package com.example.mohago_nocar.festival.domain.repository;

import com.example.mohago_nocar.festival.domain.model.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalRepository {

    Page<Festival> getFestivals(Pageable pageable);
}
