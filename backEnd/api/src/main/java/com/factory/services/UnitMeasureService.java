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
import com.factory.services.exceptions.EntityNotFoundException;

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
                                                 .orElseThrow(() -> new EntityNotFoundException("Unit of Measure not found with id: " + id));
        return UnitMeasureMapper.toDTO(entity);
    }

    @Transactional
    public UnitMeasureResponseDTO update(Integer id, UnitMeasureRequestDTO dto) {
        UnitMeasure unit = unitMeasureRepository.findById(id)
                                                 .orElseThrow(() -> new EntityNotFoundException("Unit of Measure not found with id: " + id));
        
        //Validation, update only fields that are not null in the DTO request                                         
        if (dto.unitName() != null) {
            unit.setUnitName(dto.unitName());
        }                    
        if (dto.unitSymbol() != null) {
             unit.setUnitSymbol(dto.unitSymbol());
        }
        
        return UnitMeasureMapper.toDTO(unitMeasureRepository.save(unit));
    }

     @Transactional
     public void delete(Integer id) {
         UnitMeasure entity = unitMeasureRepository.findById(id)
                                                   .orElseThrow(() -> new EntityNotFoundException("Unit of Measure not found with id: " + id));
                                                   
         unitMeasureRepository.delete(entity);
     }
}
