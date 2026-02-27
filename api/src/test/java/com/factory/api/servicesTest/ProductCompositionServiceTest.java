package com.factory.api.servicesTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.factory.model.ProductCompositionId;
import com.factory.repository.ProductCompositionRepository;
import com.factory.services.ProductCompositionService;
import com.factory.services.exceptions.BusinessRuleException;

@ExtendWith(MockitoExtension.class)
class ProductCompositionServiceTest {

    @Mock
    private ProductCompositionRepository repository;

    @InjectMocks
    private ProductCompositionService service;

    @Test
    @DisplayName("A BusinessRuleException must be thrown if the composition already exists.")
    void shouldThrowExceptionWhenCompositionAlreadyExists() {
        // Arrange
        Integer prodId = 1;
        Integer matId = 1;
        ProductCompositionId id = new ProductCompositionId(prodId, matId);
        
        when(repository.existsById(id)).thenReturn(true);

        // Act & Assert (Faltou chamar o service aqui)
        assertThrows(BusinessRuleException.class, () -> {
            // Passamos null no DTO pois a exceção deve estourar antes de usá-lo
            service.create(new com.factory.dto.request.ProductCompositionRequestDTO(prodId, matId, java.math.BigDecimal.ONE));
        });
    }
}