package com.tin.backend.services;

import com.tin.backend.domain.entities.CurEmpEntity;

import java.util.List;
import java.util.Optional;

public interface CurEmpService {

    CurEmpEntity saveCurEmp(CurEmpEntity curEmpEntity);

    List<CurEmpEntity> findAll();

    Optional<CurEmpEntity> findOne(String currentEmpTIN);

    boolean isExists(String currentEmpTIN);

    CurEmpEntity partialUpdate(String currentEmpTIN, CurEmpEntity curEmpEntity);

    void delete(String currentEmpTIN);

    CurEmpEntity UpdateCurEmp(String currentEmpTIN, CurEmpEntity curEmpEntity);
}
