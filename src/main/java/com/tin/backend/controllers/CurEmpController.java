package com.tin.backend.controllers;

import com.tin.backend.domain.dto.CurEmpDTO;
import com.tin.backend.domain.entities.CurEmpEntity;
import com.tin.backend.mappers.Mapper;
import com.tin.backend.services.CurEmpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/currentEmp")
public class CurEmpController {

    private final Mapper<CurEmpEntity, CurEmpDTO> curEmpMapper;
    private final CurEmpService curEmpService;

    public CurEmpController(
            Mapper<CurEmpEntity, CurEmpDTO> curEmpMapper,
            CurEmpService curEmpService
    ) {
        this.curEmpMapper = curEmpMapper;
        this.curEmpService = curEmpService;
    }

    // =======================
    // CREATE CURRENT EMPLOYER
    // =======================
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CurEmpDTO createCurEmp(@Valid @RequestBody CurEmpDTO curEmpDTO) {
        CurEmpEntity entity = curEmpMapper.mapFrom(curEmpDTO);
        CurEmpEntity saved = curEmpService.saveCurEmp(entity);
        return curEmpMapper.mapTo(saved);
    }

    // =======================
    // GET ALL
    // =======================
    @GetMapping
    public List<CurEmpDTO> listCurrentEmp() {
        return curEmpService.findAll()
                .stream()
                .map(curEmpMapper::mapTo)
                .collect(Collectors.toUnmodifiableList());
    }

    // =======================
    // GET BY ID (CLEAN + CONSISTENT)
    // =======================
    @GetMapping("/{id}")
    public ResponseEntity<CurEmpDTO> getById(@PathVariable String id) {

        Optional<CurEmpEntity> found = curEmpService.findOne(id);

        return found
                .map(entity -> ResponseEntity.ok(curEmpMapper.mapTo(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    // =======================
    // UPDATE
    // =======================
    @PutMapping("/{id}")
    public ResponseEntity<CurEmpDTO> update(
            @PathVariable String id,
            @RequestBody CurEmpDTO curEmpDTO
    ) {

        CurEmpEntity entity = curEmpMapper.mapFrom(curEmpDTO);
        CurEmpEntity saved = curEmpService.UpdateCurEmp(id, entity);

        return ResponseEntity.ok(curEmpMapper.mapTo(saved));
    }

    // =======================
    // PARTIAL UPDATE
    // =======================
    @PatchMapping("/{id}")
    public ResponseEntity<CurEmpDTO> partialUpdate(
            @PathVariable String id,
            @RequestBody CurEmpDTO curEmpDTO
    ) {

        if (!curEmpService.isExists(id)) {
            return ResponseEntity.notFound().build();
        }

        CurEmpEntity entity = curEmpMapper.mapFrom(curEmpDTO);
        CurEmpEntity updated = curEmpService.partialUpdate(id, entity);

        return ResponseEntity.ok(curEmpMapper.mapTo(updated));
    }

    // =======================
    // DELETE
    // =======================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        curEmpService.delete(id);
        return ResponseEntity.noContent().build();
    }
}