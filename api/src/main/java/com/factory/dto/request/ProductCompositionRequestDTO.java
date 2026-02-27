package com.factory.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductCompositionRequestDTO(

    @NotNull(message = "Product ID is required")
    Integer prodId,

    @NotNull(message = "Material ID is required")
    Integer matId,

    @NotNull(message = "Quantity needed is required")
    @PositiveOrZero(message = "Quantity needed must be positive")
    BigDecimal quantityNeeded
    
) {}
