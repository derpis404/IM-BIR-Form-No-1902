package com.tin.backend.services.impl;

import com.tin.backend.domain.entities.SpouseEntity;
import com.tin.backend.domain.entities.TaxpayerEntity;
import com.tin.backend.repository.SpouseRepository;
import com.tin.backend.repository.TaxpayerRepository;
import com.tin.backend.services.SpouseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SpouseServiceImpl implements SpouseService {

    private final SpouseRepository spouseRepository;
    private final TaxpayerRepository taxpayerRepository;

    public SpouseServiceImpl(SpouseRepository spouseRepository,
                             TaxpayerRepository taxpayerRepository) {
        this.spouseRepository = spouseRepository;
        this.taxpayerRepository = taxpayerRepository;
    }

    @Override
    public SpouseEntity saveSpouse(SpouseEntity spouseEntity) {
        return spouseRepository.save(spouseEntity);
    }

    @Override
    public List<SpouseEntity> findAll() {
        return spouseRepository.findAll();
    }

    @Override
    public Optional<SpouseEntity> findOne(String spouseTIN) {
        return spouseRepository.findById(spouseTIN);
    }

    @Override
    public boolean existsByTaxpayer(String tinId) {
        return spouseRepository.existsByTaxpayer_tinID(tinId);
    }

    @Override
    public Optional<SpouseEntity> findByTaxpayer(String tinId) {
        return spouseRepository.findByTaxpayer_tinID(tinId);
    }

    @Override
    public SpouseEntity partialUpdate(String spouseTIN, SpouseEntity spouseEntity) {
        return spouseRepository.findById(spouseTIN)
                .map(existing -> {

                    existing.setSpouseName(spouseEntity.getSpouseName());
                    existing.setSpouseEmpStatus(spouseEntity.getSpouseEmpStatus());
                    existing.setSpouseEmpName(spouseEntity.getSpouseEmpName());
                    existing.setSpouseEmpTIN(spouseEntity.getSpouseEmpTIN());

                    if (spouseEntity.getTaxpayer() != null &&
                        spouseEntity.getTaxpayer().getTinID() != null) {

                        TaxpayerEntity t = taxpayerRepository
                                .findById(spouseEntity.getTaxpayer().getTinID())
                                .orElseThrow(() -> new RuntimeException("Taxpayer not found"));

                        existing.setTaxpayer(t);
                    }

                    return spouseRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Spouse not found"));
    }

@Override
@Transactional
public void delete(String spouseTin) {

    SpouseEntity spouse = spouseRepository.findById(spouseTin)
            .orElseThrow(() -> new RuntimeException("Spouse not found"));

    TaxpayerEntity taxpayer = spouse.getTaxpayer();

    if (taxpayer != null) {

        taxpayer.setSpouse(null);
        spouse.setTaxpayer(null);

        taxpayerRepository.save(taxpayer);
    }

    spouseRepository.delete(spouse);
}
}