package com.factory.dto.response;

import java.math.BigDecimal;

public record RawMaterialResponseDTO(
    Integer    matId,
    String     matName,
    BigDecimal matQuantity,
    Integer    unitId,
    String     unitName,
    String     unitSymbol
    
) {}
