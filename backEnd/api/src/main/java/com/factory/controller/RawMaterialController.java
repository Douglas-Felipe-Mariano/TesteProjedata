package com.factory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.factory.dto.request.RawMaterialRequestDTO;
import com.factory.dto.response.RawMaterialResponseDTO;
import com.factory.services.RawMaterialService;

/**
 * Controller REST para gerenciamento de matérias-primas.
 * Expõe endpoints CRUD para operações com materiais.
 */
@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @PostMapping
    public ResponseEntity<RawMaterialResponseDTO> create(@RequestBody RawMaterialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMaterialService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Iterable<RawMaterialResponseDTO>> findAll() {
        return ResponseEntity.ok(rawMaterialService.findAll());
    }

    @GetMapping("/{matId}")
    public ResponseEntity<RawMaterialResponseDTO> findById(@PathVariable Integer matId) {
        return ResponseEntity.ok(rawMaterialService.findById(matId));
    }

    @PutMapping("/{matId}")
    public ResponseEntity<RawMaterialResponseDTO> update(@PathVariable Integer matId, @RequestBody RawMaterialRequestDTO dto) {
        return ResponseEntity.ok(rawMaterialService.update(matId, dto));
    }   

    @GetMapping("/name/{matName}")
    public ResponseEntity<Iterable<RawMaterialResponseDTO>> findByName(@PathVariable String matName) {
        return ResponseEntity.ok(rawMaterialService.findByName(matName));
    }

    @DeleteMapping("/{matId}")
    public ResponseEntity<Void> delete(@PathVariable Integer matId) {
        rawMaterialService.delete(matId);
        return ResponseEntity.noContent().build();
    }

}
