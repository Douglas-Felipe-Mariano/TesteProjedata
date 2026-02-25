package com.factory.dto.response;

import java.math.BigDecimal;

public record ProductResponseDTO(
    Integer    prodId,
    String     prodName,
    BigDecimal prodPrice,
    String     prodDescription
) {}
