package com.factory.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDTO(

    @NotBlank
    String     prodName,

    @NotNull
    @Positive
    BigDecimal prodPrice,

    String     prodDescription
) {}
