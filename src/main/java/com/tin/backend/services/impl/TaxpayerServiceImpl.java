package com.tin.backend.services.impl;

import com.tin.backend.domain.entities.*;
import com.tin.backend.repository.CurEmpRepository;
import com.tin.backend.repository.PrevEmpRepository;
import com.tin.backend.repository.SpouseRepository;
import com.tin.backend.repository.TaxpayerRepository;
import com.tin.backend.services.TaxpayerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaxpayerServiceImpl implements TaxpayerService {

    private final TaxpayerRepository taxpayerRepository;
    private final CurEmpRepository curEmpRepository;
    private final SpouseRepository spouseRepository;
    private final PrevEmpRepository prevEmpRepository;

    public TaxpayerServiceImpl(TaxpayerRepository taxpayerRepository,
                               CurEmpRepository curEmpRepository,
                               SpouseRepository spouseRepository,
                               PrevEmpRepository prevEmpRepository) {
        this.taxpayerRepository = taxpayerRepository;
        this.curEmpRepository = curEmpRepository;
        this.spouseRepository = spouseRepository;
        this.prevEmpRepository = prevEmpRepository;
    }

    // =========================
    // SAVE TAXPAYER (FIXED)
    // =========================
    @Override
    public TaxpayerEntity saveTaxpayer(TaxpayerEntity taxpayerEntity) {

        if (taxpayerEntity.getCurrentEmployer() != null &&
            taxpayerEntity.getCurrentEmployer().getCurrentEmpTIN() != null) {

            String tin = taxpayerEntity.getCurrentEmployer().getCurrentEmpTIN();

            CurEmpEntity employer = curEmpRepository.findById(tin)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Employer not found: " + tin));

            taxpayerEntity.setCurrentEmployer(employer);
        } else {
            taxpayerEntity.setCurrentEmployer(null);
        }

        return taxpayerRepository.save(taxpayerEntity);
    }

    // =========================
    // FULL UPDATE (FIXED)
    // =========================
    @Override
    public TaxpayerEntity fullUpdateTaxpayer(String TinID, TaxpayerEntity taxpayerEntity) {

        TaxpayerEntity existing = taxpayerRepository.findById(TinID)
                .orElseThrow(() -> new EntityNotFoundException("Taxpayer Not Found"));

        if (taxpayerEntity.getCurrentEmployer() != null &&
            taxpayerEntity.getCurrentEmployer().getCurrentEmpTIN() != null) {

            String tin = taxpayerEntity.getCurrentEmployer().getCurrentEmpTIN();

            CurEmpEntity employer = curEmpRepository.findById(tin)
                    .orElseThrow(() -> new EntityNotFoundException("Employer Not Found"));

            existing.setCurrentEmployer(employer);

        } else {
            existing.setCurrentEmployer(null);
        }

        return taxpayerRepository.save(existing);
    }

    // =========================
    // PARTIAL UPDATE (FIXED)
    // =========================
    @Override
    public TaxpayerEntity partialUpdate(String TinID, TaxpayerEntity taxpayerEntity) {

        return taxpayerRepository.findById(TinID)
                .map(existing -> {

                    if (taxpayerEntity.getCurrentEmployer() != null &&
                        taxpayerEntity.getCurrentEmployer().getCurrentEmpTIN() != null) {

                        String tin = taxpayerEntity.getCurrentEmployer().getCurrentEmpTIN();

                        CurEmpEntity employer = curEmpRepository.findById(tin)
                                .orElseThrow(() -> new EntityNotFoundException("Employer Not Found"));

                        existing.setCurrentEmployer(employer);
                    }

                    return taxpayerRepository.save(existing);

                }).orElseThrow(() -> new RuntimeException("Taxpayer does not exist"));
    }

    // =========================
    // FIND ALL
    // =========================
    @Override
    public List<TaxpayerEntity> findAll() {
        return StreamSupport.stream(taxpayerRepository.findAll().spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<TaxpayerEntity> findOne(String TinID) {
        return taxpayerRepository.findById(TinID);
    }

    @Override
    public boolean isExists(String TinID) {
        return taxpayerRepository.existsById(TinID);
    }

    // =========================
    // DELETE (CASCADES TO SPOUSE, CURRENT EMPLOYER, PREVIOUS/CONCURRENT EMPLOYERS)
    // =========================
    @Override
    @Transactional
    public void delete(String tinID) {

        TaxpayerEntity existing = taxpayerRepository.findById(tinID)
                .orElseThrow(() -> new EntityNotFoundException("Taxpayer not found"));

        SpouseEntity spouse = existing.getSpouse();
        CurEmpEntity currentEmployer = existing.getCurrentEmployer();
        Set<PrevEmpEntity> prevEmployers = new HashSet<>(existing.getPrevEmp());

        for (PrevEmpEntity prevEmp : prevEmployers) {
            prevEmp.getTaxpayers().remove(existing);
        }
        existing.getPrevEmp().clear();

        if (spouse != null) {
            spouse.setTaxpayer(null);
            existing.setSpouse(null);
        }

        taxpayerRepository.delete(existing);


        if (spouse != null) {
            spouseRepository.delete(spouse);
        }

        if (currentEmployer != null) {
            boolean stillReferenced = !taxpayerRepository
                    .findByCurrentEmployer(currentEmployer.getCurrentEmpTIN())
                    .isEmpty();

            if (!stillReferenced) {
                curEmpRepository.delete(currentEmployer);
            }
        }

        for (PrevEmpEntity prevEmp : prevEmployers) {
            if (prevEmp.getTaxpayers().isEmpty()) {
                prevEmpRepository.delete(prevEmp);
            } else {
                prevEmpRepository.save(prevEmp);
            }
        }
    }
}