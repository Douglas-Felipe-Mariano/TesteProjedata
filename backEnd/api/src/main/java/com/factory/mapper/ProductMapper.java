package com.factory.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.factory.dto.request.ProductRequestDTO;
import com.factory.dto.response.ProductResponseDTO;
import com.factory.model.Product;

public class ProductMapper {

    
    public static ProductResponseDTO toDTO(Product entity) {
        return new ProductResponseDTO(
            entity.getProdId(),
            entity.getProdName(),
            entity.getProdPrice(),
            entity.getProdDescription(),
            entity.getCompositions() != null ? 
                entity.getCompositions().stream()
                      .map(ProductCompositionMapper::toDTO)
                      .collect(Collectors.toList()) : Collections.emptyList()
        );
    }
    
    public static Product toEntity(ProductRequestDTO dto) {
        Product entity = new Product();
        entity.setProdName(dto.prodName());
        entity.setProdPrice(dto.prodPrice());
        entity.setProdDescription(dto.prodDescription());
        return entity;
    }
}