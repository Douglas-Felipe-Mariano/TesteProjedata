package com.factory.dto.productsugestion;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSuggestionResponseDTO(
    List<ProductionSuggestionItemDTO> items,
    BigDecimal totalValue
    
) {}
