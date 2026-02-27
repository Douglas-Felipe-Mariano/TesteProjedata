package com.factory.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RawMaterialRequestDTO(

    @NotBlank(message = "Name is required")
    String matName,

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be positive")
    BigDecimal matQuantity,

    @NotNull(message = "Unit of Measure is required")
    Integer unitId

) {}
