package com.factory.controller;

import java.util.List;

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

import com.factory.dto.request.ProductRequestDTO;
import com.factory.dto.response.ProductResponseDTO;
import com.factory.services.ProductService;

import jakarta.validation.Valid;

/**
 * REST Controller for Product management.
 * Exposes CRUD endpoints for product operations.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // POST - Create new product
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    // GET - List all products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }   

    // GET - Find product by ID
    @GetMapping("/{prodId}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Integer prodId) {
        return ResponseEntity.ok(productService.findById(prodId));
    }

    // GET - Search products by name
    @GetMapping("/name/{prodName}")
    public ResponseEntity<List<ProductResponseDTO>> findByName(@PathVariable String prodName) {
        return ResponseEntity.ok(productService.findByName(prodName));
    }

    // PUT - Update existing product
    @PutMapping("/{prodId}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Integer prodId, @org.springframework.web.bind.annotation.RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(prodId, dto));
    }

    // DELETE - Remove product
    @DeleteMapping("/{prodId}")
    public ResponseEntity<Void> delete(@PathVariable Integer prodId) {
        productService.delete(prodId);
        return ResponseEntity.noContent().build();
    }

}
