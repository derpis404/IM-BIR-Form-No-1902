package com.tin.backend.services;

import com.tin.backend.domain.entities.SpouseEntity;

import java.util.List;
import java.util.Optional;

public interface SpouseService {

    SpouseEntity saveSpouse(SpouseEntity spouseEntity);

    Optional<SpouseEntity> findByTaxpayer(String tinId);

    boolean existsByTaxpayer(String tinId);

    Optional<SpouseEntity> findOne(String spouseTIN);

    List<SpouseEntity> findAll();

    SpouseEntity partialUpdate(String spouseTIN, SpouseEntity spouseEntity);

    void delete(String spouseTIN);
}