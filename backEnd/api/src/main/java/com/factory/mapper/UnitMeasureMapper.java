package com.factory.mapper;

import com.factory.dto.request.UnitMeasureRequestDTO;
import com.factory.dto.response.UnitMeasureResponseDTO;
import com.factory.model.UnitMeasure;

public class UnitMeasureMapper {
    
    public static UnitMeasureResponseDTO toDTO(UnitMeasure entity){
        return new UnitMeasureResponseDTO(
            entity.getUnitId(),
            entity.getUnitName(),
            entity.getUnitSymbol()
        );
    }

    public static UnitMeasure toEntity(UnitMeasureRequestDTO dto) {
        UnitMeasure entity = new UnitMeasure();
        entity.setUnitName(dto.unitName());
        entity.setUnitSymbol(dto.unitSymbol()); 
        return entity;
    }

}
