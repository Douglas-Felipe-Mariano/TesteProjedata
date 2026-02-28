package com.factory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.factory.dto.request.ProductRequestDTO;
import com.factory.dto.response.ProductResponseDTO;
import com.factory.mapper.ProductMapper;
import com.factory.model.Product;
import com.factory.repository.ProductRepository;
import com.factory.services.exceptions.EntityNotFoundException;

/**
 * Business service layer for Product operations.
 * Handles validation rules and data persistence logic.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Creates a new product and returns the response DTO
    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto){
        Product product = ProductMapper.toEntity(dto);
        return ProductMapper.toDTO(productRepository.save(product));
    }

    // Retrieves all products from database
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll(){
        return productRepository.findAll()
                                .stream()
                                .map(ProductMapper::toDTO)
                                .collect(Collectors.toList());
    }

    // Finds product by ID or throws exception if not found
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Integer id){
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return ProductMapper.toDTO(product);
    }

    // Searches products by name
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByName(String prodName){
        Product product = productRepository.findByProdName(prodName);

        if (product == null) {
            throw new EntityNotFoundException("Product not found with name: " + prodName);
        }
        return List.of(ProductMapper.toDTO(product));
    }                                

    // Updates only non-null fields from DTO (partial update pattern)
    @Transactional
    public ProductResponseDTO update(Integer id, ProductRequestDTO dto){
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        if (dto.prodName() != null) {
            product.setProdName(dto.prodName());
        }                    
        if (dto.prodPrice() != null) {
             product.setProdPrice(dto.prodPrice());
        }
        if (dto.prodDescription() != null) {
             product.setProdDescription(dto.prodDescription());
        }

        return ProductMapper.toDTO(productRepository.save(product));
    }

    // Remove produto do banco de dados
    @Transactional
    public void delete(Integer id){
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }
}
