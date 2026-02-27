package com.factory.api.servicesTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.factory.dto.productsugestion.ProductionSuggestionResponseDTO;
import com.factory.model.Product;
import com.factory.model.ProductComposition;
import com.factory.model.RawMaterial;
import com.factory.repository.ProductCompositionRepository;
import com.factory.repository.ProductRepository;
import com.factory.repository.RawMaterialRepository;
import com.factory.services.ProductionPlanningService;

@ExtendWith(MockitoExtension.class)
class ProductionPlanningServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private RawMaterialRepository rawMaterialRepository;
    @Mock
    private ProductCompositionRepository productCompositionRepository;

    @InjectMocks
    private ProductionPlanningService planningService;

    @Test
    @DisplayName("Suggests production based on profit priority and virtual stock availability.")
    void shouldSuggestProductionPrioritizingProfit() {
        
        Product p1 = new Product();
        p1.setProdId(1); p1.setProdName("Produto Luxo"); p1.setProdPrice(new BigDecimal("1000.00"));

        RawMaterial m1 = new RawMaterial();
        m1.setMatId(1); m1.setMatName("Ouro"); m1.setMatQuantity(new BigDecimal("10.00"));

        ProductComposition c1 = new ProductComposition();
        c1.setProduct(p1); c1.setRawMaterial(m1); c1.setQuantityNeeded(new BigDecimal("3.00"));

        when(productRepository.findAll()).thenReturn(List.of(p1));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(m1));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(c1));

        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        assertEquals(1, response.items().size());
        assertEquals(3, response.items().get(0).quantityToProduce());
        assertEquals(new BigDecimal("3000.00"), response.totalValue());
        
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Returns an empty list or zero when there is insufficient stock.")
    void shouldReturnZeroUnitsWhenStockIsInsufficient() {

        Product p = new Product();
        p.setProdId(1); p.setProdPrice(new BigDecimal("100.00"));

        RawMaterial m = new RawMaterial();
        m.setMatId(1); m.setMatQuantity(BigDecimal.ZERO); 

        ProductComposition c = new ProductComposition();
        c.setProduct(p); c.setRawMaterial(m); c.setQuantityNeeded(new BigDecimal("1.00"));

        when(productRepository.findAll()).thenReturn(List.of(p));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(m));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(c));

        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        assertTrue(response.items().isEmpty());
        assertEquals(BigDecimal.ZERO, response.totalValue());
}
}