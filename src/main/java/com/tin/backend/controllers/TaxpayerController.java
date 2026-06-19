package com.tin.backend.controllers;

import com.tin.backend.domain.dto.TaxpayerDTO;
import com.tin.backend.domain.entities.TaxpayerEntity;
import com.tin.backend.mappers.Mapper;
import com.tin.backend.services.SpouseService;
import com.tin.backend.services.TaxpayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class TaxpayerController {

    private final TaxpayerService taxpayerService;
    private final SpouseService spouseService;
    private final Mapper<TaxpayerEntity, TaxpayerDTO> taxpayerMapper;

    public TaxpayerController(
            TaxpayerService taxpayerService,
            SpouseService spouseService,
            Mapper<TaxpayerEntity, TaxpayerDTO> taxpayerMapper) {
        this.taxpayerService = taxpayerService;
        this.spouseService = spouseService;
        this.taxpayerMapper = taxpayerMapper;
    }

    // =========================
    // CREATE TAXPAYER
    // =========================
    @PostMapping("/register/taxpayer")
    @ResponseStatus(HttpStatus.CREATED)
    public TaxpayerDTO createTaxpayer(@Valid @RequestBody TaxpayerDTO taxpayer) {

        TaxpayerEntity entity = taxpayerMapper.mapFrom(taxpayer);

        TaxpayerEntity saved = taxpayerService.saveTaxpayer(entity);

        return taxpayerMapper.mapTo(saved);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping("/taxpayer")
    public List<TaxpayerDTO> listTaxpayers() {

        return taxpayerService.findAll()
                .stream()
                .map(taxpayerMapper::mapTo)
                .collect(Collectors.toUnmodifiableList());
    }

    // =========================
    // GET ONE
    // =========================
    @GetMapping("/taxpayer/{TinID}")
    public ResponseEntity<TaxpayerDTO> getTaxpayer(@PathVariable String TinID) {

        return taxpayerService.findOne(TinID)
                .map(entity -> ResponseEntity.ok(taxpayerMapper.mapTo(entity)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // =========================
    // FULL UPDATE 
    // =========================
    @PutMapping("/taxpayer/{TinID}")
    public ResponseEntity<TaxpayerDTO> fullUpdateTaxpayer(
            @PathVariable String TinID,
            @RequestBody TaxpayerDTO taxpayerDTO) {

        if (!taxpayerService.isExists(TinID)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taxpayerDTO.setTinID(TinID);

        TaxpayerEntity entity = taxpayerMapper.mapFrom(taxpayerDTO);

        TaxpayerEntity updated = taxpayerService.fullUpdateTaxpayer(TinID, entity);

        return ResponseEntity.ok(taxpayerMapper.mapTo(updated));
    }

    // =========================
    // PARTIAL UPDATE
    // =========================
    @PatchMapping("/taxpayer/{TinID}")
    public ResponseEntity<TaxpayerDTO> partialUpdate(
            @PathVariable String TinID,
            @RequestBody TaxpayerDTO taxpayerDTO) {

        if (!taxpayerService.isExists(TinID)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TaxpayerEntity entity = taxpayerMapper.mapFrom(taxpayerDTO);

        TaxpayerEntity updated = taxpayerService.partialUpdate(TinID, entity);

        return ResponseEntity.ok(taxpayerMapper.mapTo(updated));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/taxpayer/{TinID}")
    public ResponseEntity<Void> deleteTaxpayer(@PathVariable String TinID) {

        taxpayerService.delete(TinID);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =========================
    // VALIDATION HANDLER
    // =========================
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(field, msg);
        });

        return errors;
    }
}