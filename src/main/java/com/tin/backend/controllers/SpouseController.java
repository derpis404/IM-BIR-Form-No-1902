package com.tin.backend.controllers;

import com.tin.backend.domain.dto.SpouseDTO;
import com.tin.backend.domain.entities.SpouseEntity;
import com.tin.backend.domain.entities.TaxpayerEntity;
import com.tin.backend.mappers.Mapper;
import com.tin.backend.services.SpouseService;
import com.tin.backend.repository.TaxpayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spouse")
@CrossOrigin(origins = "*")
public class SpouseController {

    private final SpouseService spouseService;
    private final Mapper<SpouseEntity, SpouseDTO> spouseMapper;
    private final TaxpayerRepository taxpayerRepository;

    public SpouseController(SpouseService spouseService,
                            Mapper<SpouseEntity, SpouseDTO> spouseMapper,
                            TaxpayerRepository taxpayerRepository) {
        this.spouseService = spouseService;
        this.spouseMapper = spouseMapper;
        this.taxpayerRepository = taxpayerRepository;
    }

    // CREATE
    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody SpouseDTO dto) {
        String tin = dto.getTaxpayer().getTinID();

        if (spouseService.existsByTaxpayer(tin)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Spouse already exists for this taxpayer");
        }

        SpouseEntity entity = new SpouseEntity();

        entity.setSpouseTIN(dto.getSpouseTIN());
        entity.setSpouseName(dto.getSpouseName());
        entity.setSpouseEmpStatus(dto.getSpouseEmpStatus());
        entity.setSpouseEmpName(dto.getSpouseEmpName());
        entity.setSpouseEmpTIN(dto.getSpouseEmpTIN());

        TaxpayerEntity taxpayer = taxpayerRepository.findById(tin)
                .orElseThrow(() -> new RuntimeException("Taxpayer not found"));

        entity.setTaxpayer(taxpayer);

        return ResponseEntity.ok(
                spouseMapper.mapTo(spouseService.saveSpouse(entity))
        );
    }

    @GetMapping
    public ResponseEntity<List<SpouseDTO>> getAll() {

        List<SpouseDTO> list = spouseService.findAll()
                .stream()
                .map(spouseMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // GET BY TAXPAYER
    @GetMapping("/by-taxpayer/{tinId}")
    public ResponseEntity<?> getByTaxpayer(@PathVariable String tinId) {

        return spouseService.findByTaxpayer(tinId)
                .map(spouseMapper::mapTo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PatchMapping("/{spouseTIN}")
    public ResponseEntity<?> update(@PathVariable String spouseTIN,
                                    @RequestBody SpouseDTO dto) {

        if (spouseService.findOne(spouseTIN).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SpouseEntity updated = spouseService.partialUpdate(
                spouseTIN,
                spouseMapper.mapFrom(dto)
        );

        return ResponseEntity.ok(spouseMapper.mapTo(updated));
    }

    // DELETE
    @DeleteMapping("/{spouseTIN}")
    public ResponseEntity<?> delete(@PathVariable String spouseTIN) {
        spouseService.delete(spouseTIN);
        return ResponseEntity.noContent().build();
    }
}