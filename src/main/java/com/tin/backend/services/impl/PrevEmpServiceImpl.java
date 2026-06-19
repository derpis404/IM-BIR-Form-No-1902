package com.tin.backend.services.impl;

import com.tin.backend.domain.entities.PrevEmpEntity;
import com.tin.backend.domain.entities.TaxpayerEntity;
import com.tin.backend.repository.PrevEmpRepository;
import com.tin.backend.repository.TaxpayerRepository;
import com.tin.backend.services.PrevEmpService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PrevEmpServiceImpl implements PrevEmpService {

    private final PrevEmpRepository prevEmpRepository;
    private final TaxpayerRepository taxpayerRepository;

    public PrevEmpServiceImpl(PrevEmpRepository prevEmpRepository, TaxpayerRepository taxpayerRepository) {
        this.prevEmpRepository = prevEmpRepository;
        this.taxpayerRepository = taxpayerRepository;
    }

    @Override
    @Transactional
    public PrevEmpEntity savePrevEmp(PrevEmpEntity prevEmpEntity, Set<String> taxpayerTinIds) {

        // If a record with this TIN already exists (e.g. another taxpayer already
        // shares this same previous/concurrent employer), merge into ITS existing
        // taxpayers instead of overwriting them with a fresh, near-empty set.
        PrevEmpEntity target = prevEmpRepository.findById(prevEmpEntity.getMultiEmpTIN())
                .orElse(null);

        if (target != null) {
            target.setMultiEmpName(prevEmpEntity.getMultiEmpName());
        } else {
            target = prevEmpEntity;
            target.setTaxpayers(new HashSet<>());
        }

        // Process relationships if incoming IDs are provided
        if (taxpayerTinIds != null && !taxpayerTinIds.isEmpty()) {

            // Fetch matching taxpayers from database using their IDs
            List<TaxpayerEntity> fetchedTaxpayers = taxpayerRepository.findAllById(taxpayerTinIds);

            // Map BOTH sides of the Many-to-Many relationship in memory
            for (TaxpayerEntity taxpayer : fetchedTaxpayers) {
                // Add taxpayer to the employer (on top of whoever else is already linked)
                target.getTaxpayers().add(taxpayer);

                // Ensure the taxpayer's collection is initialized and link this employer back to it
                if (taxpayer.getPrevEmp() == null) {
                    taxpayer.setPrevEmp(new HashSet<>());
                }
                taxpayer.getPrevEmp().add(target);
            }
        }

        // Persist the record safely
        return prevEmpRepository.save(target);
    }

    @Override
    @Transactional
    public PrevEmpEntity updatePrevEmp(String multiEmpTIN, PrevEmpEntity updatedDetails, Set<String> taxpayerTinIds) {
        // 1. Fetch the existing employer record from the database
        PrevEmpEntity existingEmp = prevEmpRepository.findById(multiEmpTIN)
                .orElseThrow(() -> new EntityNotFoundException("Previous Employer not found with TIN: " + multiEmpTIN));

        // 2. Update basic fields
        existingEmp.setMultiEmpName(updatedDetails.getMultiEmpName());

        // 3. Handle relationship changes
        if (taxpayerTinIds != null && !taxpayerTinIds.isEmpty()) {
            List<TaxpayerEntity> fetchedTaxpayers = taxpayerRepository.findAllById(taxpayerTinIds);

            if (fetchedTaxpayers.size() != taxpayerTinIds.size()) {
                throw new EntityNotFoundException("One or more provided Taxpayer IDs do not exist.");
            }

            // Clear the old relationships on BOTH sides to prevent dangling references
            for (TaxpayerEntity oldTaxpayer : existingEmp.getTaxpayers()) {
                if (oldTaxpayer.getPrevEmp() != null) {
                    oldTaxpayer.getPrevEmp().remove(existingEmp);
                }
            }
            existingEmp.getTaxpayers().clear();


            for (TaxpayerEntity newTaxpayer : fetchedTaxpayers) {
                existingEmp.getTaxpayers().add(newTaxpayer);
                if (newTaxpayer.getPrevEmp() == null) {
                    newTaxpayer.setPrevEmp(new HashSet<>());
                }
                newTaxpayer.getPrevEmp().add(existingEmp);
            }
        } else {
            for (TaxpayerEntity oldTaxpayer : existingEmp.getTaxpayers()) {
                if (oldTaxpayer.getPrevEmp() != null) {
                    oldTaxpayer.getPrevEmp().remove(existingEmp);
                }
            }
            existingEmp.getTaxpayers().clear();
        }

        return prevEmpRepository.save(existingEmp);
    }


    @Override
    public List<PrevEmpEntity> findAll() {
        return StreamSupport.stream(prevEmpRepository.findAll().spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isExists(String MultiEmpTIN) {
        return prevEmpRepository.existsById(MultiEmpTIN);
    }

    @Override
    public Optional<PrevEmpEntity> findOne(String MultiEmpTIN) {
        return prevEmpRepository.findById(MultiEmpTIN);
    }

    @Override
    @Transactional
    public void delete(String multiEmpTIN) {
        // 1. Fetch the employer to ensure it exists
        PrevEmpEntity existingEmp = prevEmpRepository.findById(multiEmpTIN)
                .orElseThrow(() -> new EntityNotFoundException("Previous Employer not found with TIN: " + multiEmpTIN));

        // 2. Clear relationship references from the loaded Taxpayers in Java memory
        // This forces Hibernate to cleanly delete the linking rows in the join table first
        for (TaxpayerEntity taxpayer : existingEmp.getTaxpayers()) {
            if (taxpayer.getPrevEmp() != null) {
                taxpayer.getPrevEmp().remove(existingEmp);
            }
        }
        existingEmp.getTaxpayers().clear();

        // 3. Delete the parent record safely
        prevEmpRepository.delete(existingEmp);
    }

}
