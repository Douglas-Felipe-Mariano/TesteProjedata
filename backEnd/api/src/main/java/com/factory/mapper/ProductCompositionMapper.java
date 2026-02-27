package com.factory.mapper;

import java.math.BigDecimal;

import com.factory.dto.response.ProductCompositionResponseDTO;
import com.factory.model.Product;
import com.factory.model.ProductComposition;
import com.factory.model.ProductCompositionId;
import com.factory.model.RawMaterial;

public class ProductCompositionMapper {

    public static ProductCompositionResponseDTO toDTO(ProductComposition entity) {

        return new ProductCompositionResponseDTO(
                entity.getProduct().getProdId(),
                entity.getProduct().getProdName(),
                entity.getRawMaterial().getMatId(),
                entity.getRawMaterial().getMatName(),
                entity.getQuantityNeeded()
        );
    }

    public static ProductComposition toEntity(Product product, 
                                              RawMaterial rawMaterial,
                                              BigDecimal quantityNeeded) 
    {
        ProductComposition entity = new ProductComposition();
        entity.setProduct(product);
        entity.setRawMaterial(rawMaterial);
        entity.setQuantityNeeded(quantityNeeded);
        
        if (product.getProdId() != null && rawMaterial.getMatId() != null) {
            
            ProductCompositionId id = new ProductCompositionId(
                product.getProdId(),
                rawMaterial.getMatId()
            );
            entity.setId(id);
        }
        return entity;
    }

}
