package com.factory.dto.response;

import java.math.BigDecimal;
import java.util.List;


public record ProductResponseDTO(
    
    Integer                             prodId,
    String                              prodName,
    BigDecimal                          prodPrice,
    String                              prodDescription,     
    List<ProductCompositionResponseDTO> compositions

) {}
