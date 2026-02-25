package com.factory.mapper;

import com.factory.dto.response.RawMaterialResponseDTO;
import com.factory.model.RawMaterial;
import com.factory.model.UnitMeasure;

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

    public static RawMaterial toEntity(RawMaterialResponseDTO dto, UnitMeasure unit) {
        RawMaterial entity = new RawMaterial();
        entity.setMatName(dto.matName());
        entity.setMatQuantity(dto.matQuantity());
        entity.setMatUnit(unit);
        return entity;
    }

}
