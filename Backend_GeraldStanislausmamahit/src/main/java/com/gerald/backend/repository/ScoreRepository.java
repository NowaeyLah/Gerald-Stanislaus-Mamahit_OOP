// ScoreRepository.java
package com.gerald.backend.repository;

import com.gerald.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    List<Score> findByPointGreaterThan(Integer minValue);
    List<Score> findAllByOrderByCreatedAtDesc();

    // Ubah dari "score s" menjadi "scores s" sesuai nama tabel di database
    @Query(value = "SELECT * FROM scores s ORDER BY s.point DESC LIMIT :limit", nativeQuery = true)
    List<Score> findTopScores(@Param("limit") Integer limit);
}