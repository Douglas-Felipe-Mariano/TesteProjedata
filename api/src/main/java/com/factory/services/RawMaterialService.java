package com.factory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.factory.dto.request.RawMaterialRequestDTO;
import com.factory.dto.response.RawMaterialResponseDTO;
import com.factory.mapper.RawMaterialMapper;
import com.factory.model.RawMaterial;
import com.factory.model.UnitMeasure;
import com.factory.repository.RawMaterialRepository;
import com.factory.repository.UnitMeasureRepository;

@Service
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final UnitMeasureRepository unitMeasureRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository, UnitMeasureRepository unitMeasureRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
        this.unitMeasureRepository = unitMeasureRepository;
    }

    @Transactional
    public RawMaterialResponseDTO create(RawMaterialRequestDTO dto) {
        //Validation, check if the unit of measure with the given id exists in the database before creating the entity
        UnitMeasure unit = unitMeasureRepository.findById(dto.unitId())
                                             .orElseThrow(() -> new RuntimeException("Unit of Measure not found with id: " + dto.unitId()));

        RawMaterial material = RawMaterialMapper.toEntity(dto, unit);

        return RawMaterialMapper.toDTO(rawMaterialRepository.save(material));
    }

    @Transactional(readOnly = true)
    public List<RawMaterialResponseDTO> findAll() {
        return rawMaterialRepository.findAll()
                                   .stream()
                                   .map(RawMaterialMapper::toDTO)
                                   .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RawMaterialResponseDTO findById(Integer id) {
        RawMaterial entity = rawMaterialRepository.findById(id)
                                                  .orElseThrow(() -> new RuntimeException("Raw Material not found with id: " + id));

        return RawMaterialMapper.toDTO(entity);
    }

     @Transactional
     public RawMaterialResponseDTO update(Integer id, RawMaterialRequestDTO dto) {
        //Validation, check if the raw material with the given id exists in the database before updating the entity
        RawMaterial entity = rawMaterialRepository.findById(id)
                                                  .orElseThrow(() -> new RuntimeException("Raw Material not found with id: " + id));
        
        //Validation, update only fields that are not null in the DTO request                                         
        if (dto.matName() != null) {
            entity.setMatName(dto.matName());
        }                    
        if (dto.matQuantity() != null) {
              entity.setMatQuantity(dto.matQuantity());
        }
        if (dto.unitId() != null) {
            //Validation, check if the unit of measure with the given id exists in the database before updating the entity
            UnitMeasure unit = unitMeasureRepository.findById(dto.unitId())
                                                 .orElseThrow(() -> new RuntimeException("Unit of Measure not found with id: " + dto.unitId()));
            entity.setMatUnit(unit);
        }
        
        RawMaterial updatedMaterial = rawMaterialRepository.save(entity);

        return RawMaterialMapper.toDTO(updatedMaterial);
    }

      @Transactional
      public void delete(Integer id) {
        RawMaterial entity = rawMaterialRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Raw Material not found with id: " + id)); 

        rawMaterialRepository.delete(entity);                                                    
      }

    

}
