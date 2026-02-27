package com.factory.dto.response;

import java.math.BigDecimal;

public record ProductCompositionResponseDTO(
    
    Integer      prodId,
    String       prodName,
    Integer      matId,
    String       matName,
    BigDecimal   quantityNeeded
    
) {}