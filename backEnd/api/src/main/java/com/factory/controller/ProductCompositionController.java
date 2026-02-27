package com.factory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.factory.dto.request.ProductCompositionRequestDTO;
import com.factory.dto.response.ProductCompositionResponseDTO;
import com.factory.services.ProductCompositionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product-compositions")
public class ProductCompositionController {

    private final ProductCompositionService service;

    public ProductCompositionController(ProductCompositionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductCompositionResponseDTO> create(@RequestBody @Valid ProductCompositionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductCompositionResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<List<ProductCompositionResponseDTO>> findByProductId(@PathVariable Integer prodId) {
        return ResponseEntity.ok(service.findByProductId(prodId));
    }

    @DeleteMapping("/{prodId}/{matId}")
    public ResponseEntity<Void> delete(@PathVariable Integer prodId, @PathVariable Integer matId) {
        service.delete(prodId, matId);
        return ResponseEntity.noContent().build();
    }
}