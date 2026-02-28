package com.factory.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.factory.dto.productsugestion.ProductionSuggestionItemDTO;
import com.factory.dto.productsugestion.ProductionSuggestionResponseDTO;
import com.factory.model.Product;
import com.factory.model.ProductComposition;
import com.factory.model.RawMaterial;
import com.factory.repository.ProductCompositionRepository;
import com.factory.repository.ProductRepository;
import com.factory.repository.RawMaterialRepository;

@Service
public class ProductionPlanningService {

    private final ProductRepository            productRepository;
    private final RawMaterialRepository        rawMaterialRepository;
    private final ProductCompositionRepository productCompositionRepository;

    public ProductionPlanningService(ProductRepository            productRepository,
                                     RawMaterialRepository        rawMaterialRepository,
                                     ProductCompositionRepository productCompositionRepository
                                    )          
    {
        this.productRepository              = productRepository;
        this.rawMaterialRepository          = rawMaterialRepository;
        this.productCompositionRepository   = productCompositionRepository;
    }               
    
    @Transactional(readOnly = true)
    public ProductionSuggestionResponseDTO suggestProduction() {

        // Create a mutable copy of the products list for sorting
        List<Product> products = new ArrayList<>(productRepository.findAll());

        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();

        List<ProductComposition> allcompositions = productCompositionRepository.findByProductInWithMaterial(products);

        // Stock cache: maps material ID to available quantity
        Map<Integer, BigDecimal> availableStock = rawMaterials.stream()
                                                              .collect(Collectors.toMap(
                                                               RawMaterial::getMatId,
                                                               RawMaterial::getMatQuantity
                                                            ));

        // Group compositions by product ID for efficient lookup
        Map<Integer, List<ProductComposition>> compositionsByProduct = allcompositions.stream()
                                                                                   .collect(Collectors.groupingBy(
                                                                                    pc -> pc.getProduct().getProdId()
                                                                                ));
                                                            
        // Sort products by highest price first (greedy algorithm for max revenue)
        products.sort(Comparator.comparing(Product::getProdPrice).reversed());                                                                

        List<ProductionSuggestionItemDTO> result = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        
        for (Product product : products){
            
            List<ProductComposition> compositions = compositionsByProduct.get(product.getProdId());

            if (compositions == null || compositions.isEmpty()){
                continue; 
            }

            int maxUnits = calculateMaxUnits(compositions, availableStock);
            
            if(maxUnits > 0){

                //virtual stock decrase
                for(ProductComposition comp : compositions){

                    Integer matId = comp.getRawMaterial().getMatId();

                    BigDecimal needed = comp.getQuantityNeeded()
                                            .multiply(BigDecimal.valueOf(maxUnits));

                    BigDecimal currentStock = availableStock.getOrDefault(matId, BigDecimal.ZERO);                                            

                    availableStock.put(matId, currentStock.subtract(needed));
                }

                BigDecimal revenue = product.getProdPrice()
                                            .multiply(BigDecimal.valueOf(maxUnits));

                totalRevenue = totalRevenue.add(revenue);

                result.add(new ProductionSuggestionItemDTO(
                    product.getProdId(),
                    product.getProdName(),
                    maxUnits,
                    revenue
                ));
            }
        }

        return new ProductionSuggestionResponseDTO(result, totalRevenue);   
    }

    private int calculateMaxUnits(List<ProductComposition> compositions,
                                  Map<Integer, BigDecimal> stock) {

        int maxUnits = Integer.MAX_VALUE;

        for (ProductComposition comp : compositions){

            Integer matId = comp.getRawMaterial().getMatId();

            BigDecimal available = stock.get(matId);

            if (available == null || available.compareTo(BigDecimal.ZERO) <= 0 
                                  || comp.getQuantityNeeded().compareTo(BigDecimal.ZERO) <= 0){
                return 0; // No stock available for this raw material, can't produce any units
            }   

            int possible = available.divide(comp.getQuantityNeeded(), 0, RoundingMode.DOWN)
                                .intValue();

            maxUnits = Math.min(maxUnits, possible);
        }

        return maxUnits;
    }
}
