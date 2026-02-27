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



@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto){
        Product product = ProductMapper.toEntity(dto);
        
        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll(){
        return productRepository.findAll()
                                .stream()
                                .map(ProductMapper::toDTO)
                                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Integer id){
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return ProductMapper.toDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByName(String prodName){
        Product product = productRepository.findByProdName(prodName);

        if (product == null) {
            throw new EntityNotFoundException("Product not found with name: " + prodName);
        }
        return List.of(ProductMapper.toDTO(product));
    }                                

     @Transactional
     public ProductResponseDTO update(Integer id, ProductRequestDTO dto){
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        //Validation, update only fields that are not null in the DTO request                                         
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

     @Transactional
     public void delete(Integer id){
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
     }
}
