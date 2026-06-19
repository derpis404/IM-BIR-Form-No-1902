package com.tin.backend.services;

import com.tin.backend.domain.entities.TaxpayerEntity;

import java.util.List;
import java.util.Optional;

public interface TaxpayerService {

    TaxpayerEntity saveTaxpayer(TaxpayerEntity taxpayerEntity);

    List<TaxpayerEntity> findAll();

    Optional<TaxpayerEntity> findOne(String TinID);

    boolean isExists(String TinID);

    TaxpayerEntity partialUpdate(String TinID, TaxpayerEntity taxpayerEntity);

    void delete(String TinID);

    TaxpayerEntity fullUpdateTaxpayer(String TinID, TaxpayerEntity taxpayerEntity);
}
