package com.tin.backend.controllers;

import com.tin.backend.domain.dto.PrevEmpDTO;
import com.tin.backend.domain.entities.PrevEmpEntity;
import com.tin.backend.services.PrevEmpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class PrevEmpController {

    private final PrevEmpService service;

    public PrevEmpController(PrevEmpService service) {
        this.service = service;
    }

    // =========================
    // HELPER: ENTITY → DTO
    // =========================
    private PrevEmpDTO toDTO(PrevEmpEntity e) {
        PrevEmpDTO dto = new PrevEmpDTO();
        dto.setMultiEmpTIN(e.getMultiEmpTIN());
        dto.setMultiEmpName(e.getMultiEmpName());

        Set<String> ids = e.getTaxpayers()
                .stream()
                .map(t -> t.getTinID())
                .collect(Collectors.toSet());

        dto.setTaxpayerIds(ids);

        return dto;
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping("/register/prevEmp")
    public ResponseEntity<PrevEmpDTO> create(@RequestBody PrevEmpDTO dto) {

        System.out.println("DTO TAXPAYER IDS: " + dto.getTaxpayerIds());

        PrevEmpEntity entity = new PrevEmpEntity();
        entity.setMultiEmpTIN(dto.getMultiEmpTIN());
        entity.setMultiEmpName(dto.getMultiEmpName());

        Set<String> ids = dto.getTaxpayerIds();

        PrevEmpEntity saved = service.savePrevEmp(entity, ids);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDTO(saved));
    }

    // =========================
    // READ ALL
    // =========================
    @GetMapping("/prevEmp")
    public List<PrevEmpDTO> getAll() {
        return service.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // =========================
    // READ ONE
    // =========================
    @GetMapping("/prevEmp/{id}")
    public ResponseEntity<PrevEmpDTO> getOne(@PathVariable String id) {
        return service.findOne(id)
                .map(e -> ResponseEntity.ok(toDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/prevEmp/{id}")
    public ResponseEntity<PrevEmpDTO> update(@PathVariable String id,
                                            @RequestBody PrevEmpDTO dto) {

        Set<String> ids = dto.getTaxpayerIds();

        PrevEmpEntity existing = service.findOne(id)
                .orElseThrow(() -> new RuntimeException("PrevEmp not found: " + id));

        PrevEmpEntity updated = service.updatePrevEmp(
                id,
                existing,
                ids
        );

        return ResponseEntity.ok(toDTO(updated));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/prevEmp/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}