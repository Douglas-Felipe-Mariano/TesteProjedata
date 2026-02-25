package com.factory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.factory.dto.request.UnitMeasureRequestDTO;
import com.factory.dto.response.UnitMeasureResponseDTO;
import com.factory.mapper.UnitMeasureMapper;
import com.factory.model.UnitMeasure;
import com.factory.repository.UnitMeasureRepository;

@Service
public class UnitMeasureService {

    private final UnitMeasureRepository unitMeasureRepository;

    public UnitMeasureService(UnitMeasureRepository unitMeasureRepository) {
        this.unitMeasureRepository = unitMeasureRepository;
    }

    @Transactional
    public UnitMeasureResponseDTO create(UnitMeasureRequestDTO dto) {
        UnitMeasure unit = UnitMeasureMapper.toEntity(dto);
        
        return UnitMeasureMapper.toDTO(unitMeasureRepository.save(unit));
    }

    @Transactional(readOnly = true)
    public List<UnitMeasureResponseDTO> findAll() {
        return unitMeasureRepository.findAll()
                                    .stream()
                                    .map(UnitMeasureMapper::toDTO)
                                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UnitMeasureResponseDTO findById(Integer id) {
        UnitMeasure entity = unitMeasureRepository.findById(id)
                                                 .orElseThrow(() -> new RuntimeException("Unit of Measure not found with id: " + id));
        return UnitMeasureMapper.toDTO(entity);
    }

    @Transactional
    public UnitMeasureResponseDTO update(Integer id, UnitMeasureRequestDTO dto) {
        UnitMeasure entity = unitMeasureRepository.findById(id)
                                                 .orElseThrow(() -> new RuntimeException("Unit of Measure not found with id: " + id));
        
        //Validation, update only fields that are not null in the DTO request                                         
        if (dto.unitName() != null) {
            entity.setUnitName(dto.unitName());
        }                    
        if (dto.unitSymbol() != null) {
             entity.setUnitSymbol(dto.unitSymbol());
        }
        
        return UnitMeasureMapper.toDTO(entity);
    }

     @Transactional
     public void delete(Integer id) {
         UnitMeasure entity = unitMeasureRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException("Unit of Measure not found with id: " + id));
                                                   
         unitMeasureRepository.delete(entity);
     }
}
