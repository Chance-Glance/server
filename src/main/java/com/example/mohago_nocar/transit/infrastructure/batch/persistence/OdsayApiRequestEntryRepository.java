package com.example.mohago_nocar.transit.infrastructure.batch.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OdsayApiRequestEntryRepository extends JpaRepository<OdsayApiRequestEntry, Long> {

    List<OdsayApiRequestEntry> findAllByOrderByCreatedAtAsc(Pageable pageable);

}
