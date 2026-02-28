package com.factory.api.servicesTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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

/**
 * Unit tests for ProductionPlanningService.
 * Tests the production suggestion algorithm that maximizes total revenue.
 */
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
    @DisplayName("Should suggest production prioritizing highest price products first")
    void shouldSuggestProductionPrioritizingProfit() {
        // Arrange: Create a luxury product that uses 3 units of gold per unit
        Product p1 = new Product();
        p1.setProdId(1); p1.setProdName("Luxury Product"); p1.setProdPrice(new BigDecimal("1000.00"));

        RawMaterial m1 = new RawMaterial();
        m1.setMatId(1); m1.setMatName("Gold"); m1.setMatQuantity(new BigDecimal("10.00"));

        ProductComposition c1 = new ProductComposition();
        c1.setProduct(p1); c1.setRawMaterial(m1); c1.setQuantityNeeded(new BigDecimal("3.00"));

        when(productRepository.findAll()).thenReturn(List.of(p1));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(m1));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(c1));

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert: Should produce 3 units (10/3 = 3) with total revenue of 3000
        assertEquals(1, response.items().size());
        assertEquals(3, response.items().get(0).quantityToProduce());
        assertEquals(new BigDecimal("3000.00"), response.totalValue());
        
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return zero units when stock is insufficient")
    void shouldReturnZeroUnitsWhenStockIsInsufficient() {
        // Arrange: Product needs material but stock is zero
        Product p = new Product();
        p.setProdId(1); p.setProdPrice(new BigDecimal("100.00"));

        RawMaterial m = new RawMaterial();
        m.setMatId(1); m.setMatQuantity(BigDecimal.ZERO); 

        ProductComposition c = new ProductComposition();
        c.setProduct(p); c.setRawMaterial(m); c.setQuantityNeeded(new BigDecimal("1.00"));

        when(productRepository.findAll()).thenReturn(List.of(p));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(m));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(c));

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert: No products can be produced
        assertTrue(response.items().isEmpty());
        assertEquals(BigDecimal.ZERO, response.totalValue());
    }

    @Test
    @DisplayName("Should resolve raw material conflicts by prioritizing higher value products")
    void shouldResolveRawMaterialConflicts() {
        // Arrange: Two products competing for the same raw material
        // Product A: R$100, needs 10kg of fabric
        // Product B: R$50, needs 5kg of fabric
        // Stock: 15kg of fabric
        // Expected: 1 unit of A (10kg) + 1 unit of B (5kg) = R$150 total
        
        Product productA = new Product();
        productA.setProdId(1); productA.setProdName("Premium Shirt"); productA.setProdPrice(new BigDecimal("100.00"));
        
        Product productB = new Product();
        productB.setProdId(2); productB.setProdName("Basic Shirt"); productB.setProdPrice(new BigDecimal("50.00"));

        RawMaterial fabric = new RawMaterial();
        fabric.setMatId(1); fabric.setMatName("Fabric"); fabric.setMatQuantity(new BigDecimal("15.00"));

        ProductComposition compA = new ProductComposition();
        compA.setProduct(productA); compA.setRawMaterial(fabric); compA.setQuantityNeeded(new BigDecimal("10.00"));
        
        ProductComposition compB = new ProductComposition();
        compB.setProduct(productB); compB.setRawMaterial(fabric); compB.setQuantityNeeded(new BigDecimal("5.00"));

        when(productRepository.findAll()).thenReturn(List.of(productA, productB));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(fabric));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(compA, compB));

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert: Should produce 1A + 1B = R$150
        assertEquals(2, response.items().size());
        assertEquals(new BigDecimal("150.00"), response.totalValue());
        
        // First item should be the higher value product (Premium Shirt)
        assertEquals("Premium Shirt", response.items().get(0).prodName());
        assertEquals(1, response.items().get(0).quantityToProduce());
    }

    @Test
    @DisplayName("Should skip products without composition")
    void shouldSkipProductsWithoutComposition() {
        // Arrange: Product without any composition
        Product p = new Product();
        p.setProdId(1); p.setProdName("Empty Product"); p.setProdPrice(new BigDecimal("500.00"));

        RawMaterial m = new RawMaterial();
        m.setMatId(1); m.setMatQuantity(new BigDecimal("100.00"));

        when(productRepository.findAll()).thenReturn(List.of(p));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(m));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(new ArrayList<>());

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert: No products suggested because there's no composition
        assertTrue(response.items().isEmpty());
        assertEquals(BigDecimal.ZERO, response.totalValue());
    }

    @Test
    @DisplayName("Should handle multiple raw materials per product correctly")
    void shouldHandleMultipleRawMaterialsPerProduct() {
        // Arrange: Product needs 2 different raw materials
        // Product needs: 5 units of Material A AND 3 units of Material B
        // Stock: 10 units of A, 6 units of B
        // Limiting factor is B: 6/3 = 2 units can be produced
        
        Product product = new Product();
        product.setProdId(1); product.setProdName("Complex Product"); product.setProdPrice(new BigDecimal("200.00"));

        RawMaterial materialA = new RawMaterial();
        materialA.setMatId(1); materialA.setMatName("Material A"); materialA.setMatQuantity(new BigDecimal("10.00"));
        
        RawMaterial materialB = new RawMaterial();
        materialB.setMatId(2); materialB.setMatName("Material B"); materialB.setMatQuantity(new BigDecimal("6.00"));

        ProductComposition compA = new ProductComposition();
        compA.setProduct(product); compA.setRawMaterial(materialA); compA.setQuantityNeeded(new BigDecimal("5.00"));
        
        ProductComposition compB = new ProductComposition();
        compB.setProduct(product); compB.setRawMaterial(materialB); compB.setQuantityNeeded(new BigDecimal("3.00"));

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(materialA, materialB));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(compA, compB));

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert: Should produce 2 units (limited by Material B: 6/3=2)
        assertEquals(1, response.items().size());
        assertEquals(2, response.items().get(0).quantityToProduce());
        assertEquals(new BigDecimal("400.00"), response.totalValue());
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyWhenNoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        when(rawMaterialRepository.findAll()).thenReturn(new ArrayList<>());
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(new ArrayList<>());

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert
        assertTrue(response.items().isEmpty());
        assertEquals(BigDecimal.ZERO, response.totalValue());
    }

    @Test
    @DisplayName("Should maximize total revenue with greedy algorithm")
    void shouldMaximizeTotalRevenue() {
        // Arrange: Three products with different prices competing for resources
        // Product A: R$300, needs 3kg fabric (can make 4 units with 12kg)
        // Product B: R$200, needs 4kg fabric  
        // Product C: R$100, needs 2kg fabric
        // Stock: 12kg fabric
        // Greedy approach: 
        // - Process A first (highest price): 12/3 = 4 units, uses all 12kg = R$1200
        // - No stock left for B or C
        // Result: 1 product (A) with 4 units = R$1200
        
        Product productA = new Product();
        productA.setProdId(1); productA.setProdName("Luxury"); productA.setProdPrice(new BigDecimal("300.00"));
        
        Product productB = new Product();
        productB.setProdId(2); productB.setProdName("Standard"); productB.setProdPrice(new BigDecimal("200.00"));
        
        Product productC = new Product();
        productC.setProdId(3); productC.setProdName("Basic"); productC.setProdPrice(new BigDecimal("100.00"));

        RawMaterial fabric = new RawMaterial();
        fabric.setMatId(1); fabric.setMatName("Fabric"); fabric.setMatQuantity(new BigDecimal("12.00"));

        ProductComposition compA = new ProductComposition();
        compA.setProduct(productA); compA.setRawMaterial(fabric); compA.setQuantityNeeded(new BigDecimal("3.00"));
        
        ProductComposition compB = new ProductComposition();
        compB.setProduct(productB); compB.setRawMaterial(fabric); compB.setQuantityNeeded(new BigDecimal("4.00"));
        
        ProductComposition compC = new ProductComposition();
        compC.setProduct(productC); compC.setRawMaterial(fabric); compC.setQuantityNeeded(new BigDecimal("2.00"));

        when(productRepository.findAll()).thenReturn(List.of(productA, productB, productC));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(fabric));
        when(productCompositionRepository.findByProductInWithMaterial(anyList())).thenReturn(List.of(compA, compB, compC));

        // Act
        ProductionSuggestionResponseDTO response = planningService.suggestProduction();

        // Assert: Total revenue should be R$1200 (4 units x R$300)
        // Greedy algorithm produces all of the highest value product first
        assertEquals(1, response.items().size());
        assertEquals(new BigDecimal("1200.00"), response.totalValue());
        
        // Verify the highest value product is produced
        assertEquals("Luxury", response.items().get(0).prodName());
        assertEquals(4, response.items().get(0).quantityToProduce());
    }
}