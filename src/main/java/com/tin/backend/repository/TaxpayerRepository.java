package com.tin.backend.repository;

import com.tin.backend.domain.entities.CurEmpEntity;
import com.tin.backend.domain.entities.SpouseEntity;
import com.tin.backend.domain.entities.TaxpayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TaxpayerRepository extends JpaRepository<TaxpayerEntity, String> {

    @Query("SELECT t FROM TaxpayerEntity t WHERE t.currentEmployer.currentEmpTIN = :tin")
    List<TaxpayerEntity> findByCurrentEmployer(@Param("tin") String tin);
}
