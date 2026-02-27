package com.factory.dto.productsugestion;

import java.math.BigDecimal;

public record ProductionSuggestionItemDTO(
    Integer     prodId,
    String     prodName,
    Integer    quantityToProduce,
    BigDecimal totalRevenue
    
) {}
