package com.factory.mapper;

import com.factory.dto.response.RawMaterialResponseDTO;
import com.factory.model.RawMaterial;

public class RawMaterialMapper {

    public static RawMaterialResponseDTO toDTO(RawMaterial entity){
        return new RawMaterialResponseDTO(
            entity.getMatId(),
            entity.getMatName(),
            entity.getMatQuantity(),
            entity.getMatUnit().getUnitId(),
            entity.getMatUnit().getUnitName(),
            entity.getMatUnit().getUnitSymbol()
        );
    }

}
