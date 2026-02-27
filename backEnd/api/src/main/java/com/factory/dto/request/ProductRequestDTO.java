package com.factory.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequestDTO(

    @NotBlank(message = "Name is required")
    String     prodName,

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive")
    BigDecimal prodPrice,

    String     prodDescription
    
) {}
