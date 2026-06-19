package com.tin.backend.services;

import com.tin.backend.domain.entities.PrevEmpEntity;
import com.tin.backend.domain.entities.TaxpayerEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PrevEmpService {
    PrevEmpEntity updatePrevEmp(String multiEmpTIN, PrevEmpEntity updatedDetails, Set<String> taxpayerTinIds);
    List<PrevEmpEntity> findAll();
    Optional<PrevEmpEntity> findOne(String multiEmpTIN);
    boolean isExists(String multiEmpTIN);
    void delete(String multiEmpTIN);

    PrevEmpEntity savePrevEmp(PrevEmpEntity prevEmpEntity, Set<String> taxpayerTinIds);

}
