package com.factory.controller;

import com.factory.dto.request.UnitMeasureRequestDTO;
import com.factory.dto.response.UnitMeasureResponseDTO;
import com.factory.services.UnitMeasureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unit-measures")
public class UnitMeasureController {

    private final UnitMeasureService unitMeasureService;

    public UnitMeasureController(UnitMeasureService unitMeasureService) {
        this.unitMeasureService = unitMeasureService;
    }

    @PostMapping
    public ResponseEntity<UnitMeasureResponseDTO> create(@RequestBody @Valid UnitMeasureRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unitMeasureService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<UnitMeasureResponseDTO>> findAll() {
        return ResponseEntity.ok(unitMeasureService.findAll());
    }

    @GetMapping("/{unitId}")
    public ResponseEntity<UnitMeasureResponseDTO> findById(@PathVariable Integer unitId) {
        return ResponseEntity.ok(unitMeasureService.findById(unitId));
    }

    @PutMapping("/{unitId}")
    public ResponseEntity<UnitMeasureResponseDTO> update(@PathVariable Integer unitId, @RequestBody @Valid UnitMeasureRequestDTO dto) {
        return ResponseEntity.ok(unitMeasureService.update(unitId, dto));
    }

    @DeleteMapping("/{unitId}")
    public ResponseEntity<Void> delete(@PathVariable Integer unitId) {
        unitMeasureService.delete(unitId);
        return ResponseEntity.noContent().build();
    }
}