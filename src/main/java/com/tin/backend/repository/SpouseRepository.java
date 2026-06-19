package com.tin.backend.repository;

import com.tin.backend.domain.entities.SpouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpouseRepository extends JpaRepository<SpouseEntity, String> {

    Optional<SpouseEntity> findByTaxpayer_tinID(String tinId);

    boolean existsByTaxpayer_tinID(String tinId);
}