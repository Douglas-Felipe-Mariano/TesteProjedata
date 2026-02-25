package com.factory.mapper;

import com.factory.dto.request.ProductRequestDTO;
import com.factory.dto.response.ProductResponseDTO;
import com.factory.model.Product;

public class ProductMapper {

    
    public static ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
            product.getProdId(),
            product.getProdName(),
            product.getProdPrice(),
            product.getProdDescription()
        );
    }
    
    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setProdName(dto.prodName());
        product.setProdPrice(dto.prodPrice());
        product.setProdDescription(dto.prodDescription());
        return product;
    }
}