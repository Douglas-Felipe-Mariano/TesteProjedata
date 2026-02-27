package com.factory.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UnitMeasureRequestDTO(

    @NotBlank(message = "Name is required")
    String unitName,

    @NotBlank(message = "Symbol is required")
    String unitSymbol

) {}
    