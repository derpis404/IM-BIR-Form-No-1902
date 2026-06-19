package com.tin.backend.repository;

import com.tin.backend.domain.entities.CurEmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurEmpRepository extends JpaRepository<CurEmpEntity, String> {

}
