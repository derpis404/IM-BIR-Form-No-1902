package com.tin.backend.services.impl;

import com.tin.backend.domain.entities.CurEmpEntity;
import com.tin.backend.domain.entities.TaxpayerEntity;
import com.tin.backend.repository.CurEmpRepository;
import com.tin.backend.repository.TaxpayerRepository;
import com.tin.backend.services.CurEmpService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CurEmpServiceImpl implements CurEmpService {

    private CurEmpRepository curEmpRepository;
    private TaxpayerRepository taxpayerRepository;

    public CurEmpServiceImpl(CurEmpRepository curEmpRepository, TaxpayerRepository taxpayerRepository) {
        this.curEmpRepository = curEmpRepository;
        this.taxpayerRepository = taxpayerRepository;
    }

    @Override
    public CurEmpEntity UpdateCurEmp(String currentEmpTIN, CurEmpEntity curEmpEntity) {
        curEmpEntity.setCurrentEmpTIN(currentEmpTIN);
        return curEmpRepository.save(curEmpEntity);
    }

    @Override
    public CurEmpEntity saveCurEmp(CurEmpEntity curEmpEntity) {
        return curEmpRepository.save(curEmpEntity);
    }

    @Override
    public List<CurEmpEntity> findAll() {
        return StreamSupport.stream(curEmpRepository.findAll().spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isExists(String currentEmpTIN) {
        return curEmpRepository.existsById(currentEmpTIN);
    }

    @Override
    @Transactional
    public void delete(String currentEmpTIN) {

        CurEmpEntity currentEmployer = curEmpRepository.findById(currentEmpTIN)
                .orElseThrow(() -> new EntityNotFoundException("Current Employer Not Found"));

        List<TaxpayerEntity> taxpayerEntities =
                taxpayerRepository.findByCurrentEmployer(currentEmpTIN);

        for (TaxpayerEntity taxpayer : taxpayerEntities) {
            taxpayer.setCurrentEmployer(null);
        }

        taxpayerRepository.saveAll(taxpayerEntities);
        curEmpRepository.delete(currentEmployer);
    }

    @Override
    public Optional<CurEmpEntity> findOne(String currentEmpTIN) {
        return curEmpRepository.findById(currentEmpTIN);
    }

    @Override
    public CurEmpEntity partialUpdate(String currentEmpTIN, CurEmpEntity curEmpEntity) {

        curEmpEntity.setCurrentEmpTIN(currentEmpTIN);

        return curEmpRepository.findById(currentEmpTIN).map(existing -> {

            Optional.ofNullable(curEmpEntity.getCurrentEmpName())
                    .ifPresent(existing::setCurrentEmpName);

            Optional.ofNullable(curEmpEntity.getCurrentEmpAddress())
                    .ifPresent(existing::setCurrentEmpAddress);

            Optional.ofNullable(curEmpEntity.getEmpContactDetails())
                    .ifPresent(existing::setEmpContactDetails);

            return curEmpRepository.save(existing);

        }).orElseThrow(() -> new RuntimeException("Current Employee does not exist."));
    }
}