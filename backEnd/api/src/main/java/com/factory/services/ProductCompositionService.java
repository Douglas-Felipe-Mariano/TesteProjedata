package com.factory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.factory.dto.request.ProductCompositionRequestDTO;
import com.factory.dto.response.ProductCompositionResponseDTO;
import com.factory.mapper.ProductCompositionMapper;
import com.factory.model.Product;
import com.factory.model.ProductComposition;
import com.factory.model.ProductCompositionId;
import com.factory.model.RawMaterial;
import com.factory.repository.ProductCompositionRepository;
import com.factory.repository.ProductRepository;
import com.factory.repository.RawMaterialRepository;
import com.factory.services.exceptions.BusinessRuleException;
import com.factory.services.exceptions.EntityNotFoundException;

@Service
public class ProductCompositionService {

    private final ProductCompositionRepository productCompositionRepository;
    private final ProductRepository            productRepository;
    private final RawMaterialRepository        rawMaterialRepository;

    public ProductCompositionService(ProductCompositionRepository productCompositionRepository, 
                                     ProductRepository            productRepository,
                                     RawMaterialRepository        rawMaterialRepository
                                    ) 
    {
        this.productCompositionRepository = productCompositionRepository;
        this.productRepository            = productRepository;
        this.rawMaterialRepository        = rawMaterialRepository;
    }

    @Transactional
    public ProductCompositionResponseDTO create(ProductCompositionRequestDTO dto){

        //Validation, check if the product with the given id exists in the database before creating the entity
        Product product = productRepository.findById(dto.prodId())
                                           .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + dto.prodId()));      


        //Validation, check if the raw material with the given id exists in the database before creating the entity                                           
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.matId())
                                                       .orElseThrow(() -> new EntityNotFoundException("Raw Material not found with id: " + dto.matId()));                                           

        ProductCompositionId id = new ProductCompositionId(dto.prodId(), dto.matId());
        
        if (productCompositionRepository.existsById(id)) {
            throw new BusinessRuleException("This product already contains this raw material.");
        }

        ProductComposition entity = ProductCompositionMapper.toEntity(product, 
                                                                      rawMaterial,
                                                                      dto.quantityNeeded());
        
        return ProductCompositionMapper.toDTO(productCompositionRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<ProductCompositionResponseDTO> findAll(){
        List<ProductComposition> compositions = productCompositionRepository.findAllWithProductAndMaterial();


        return compositions.stream()
                           .map(ProductCompositionMapper::toDTO)
                           .collect(Collectors.toList());
    }

    @Transactional
    public List<ProductCompositionResponseDTO> findByProductId(Integer prodId) {
        return productCompositionRepository.findByProdId(prodId)
                                            .stream()
                                            .map(ProductCompositionMapper::toDTO)
                                            .collect(Collectors.toList());
    }

    @Transactional
    public ProductCompositionResponseDTO update(Integer prodId, Integer matId, ProductCompositionRequestDTO dto) {

        ProductCompositionId id = new ProductCompositionId(prodId, matId);

        ProductComposition composition = productCompositionRepository.findById(id)
                                                                .orElseThrow(() -> new EntityNotFoundException("Product Composition not found with product id: " + prodId + " and raw material id: " + matId));
        
        //Validation, update only fields that are not null in the DTO request                                         
        if (dto.quantityNeeded() != null) {
            composition.setQuantityNeeded(dto.quantityNeeded());
        }                    
        
        return ProductCompositionMapper.toDTO(productCompositionRepository.save(composition));
    }

    @Transactional
    public void delete(Integer prodId, Integer matId) {

        ProductCompositionId id = new ProductCompositionId(prodId, matId);

        ProductComposition entity = productCompositionRepository.findById(id)
                                                                .orElseThrow(() -> new EntityNotFoundException("Product Composition not found with product id: " + prodId + " and raw material id: " + matId));
        productCompositionRepository.delete(entity);
    }


}
