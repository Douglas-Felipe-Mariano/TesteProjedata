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
import com.factory.services.exceptions.EntityNotFoundException;

@Service
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final UnitMeasureRepository unitMeasureRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository, 
                              UnitMeasureRepository unitMeasureRepository
                            )
    {
        this.rawMaterialRepository = rawMaterialRepository;
        this.unitMeasureRepository = unitMeasureRepository;
    }

    @Transactional
    public RawMaterialResponseDTO create(RawMaterialRequestDTO dto) {
        //Validation, check if the unit of measure with the given id exists in the database before creating the entity
        UnitMeasure unit = unitMeasureRepository.findById(dto.unitId())
                                             .orElseThrow(() -> new EntityNotFoundException("Unit of Measure not found with id: " + dto.unitId()));

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
                                                  .orElseThrow(() -> new EntityNotFoundException("Raw Material not found with id: " + id));

        return RawMaterialMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<RawMaterialResponseDTO> findByName(String matName) {
        RawMaterial material = rawMaterialRepository.findByMatName(matName);

        if (material == null) {
            throw new EntityNotFoundException("Raw Material not found with name: " + matName);
        }
        return List.of(RawMaterialMapper.toDTO(material));
    }

     @Transactional
     public RawMaterialResponseDTO update(Integer id, RawMaterialRequestDTO dto) {
        //Validation, check if the raw material with the given id exists in the database before updating the entity
        RawMaterial material = rawMaterialRepository.findById(id)
                                                  .orElseThrow(() -> new EntityNotFoundException("Raw Material not found with id: " + id));
        
        //Validation, update only fields that are not null in the DTO request                                         
        if (dto.matName() != null) {
            material.setMatName(dto.matName());
        }                    
        if (dto.matQuantity() != null) {
              material.setMatQuantity(dto.matQuantity());
        }
        if (dto.unitId() != null) {
            //Validation, check if the unit of measure with the given id exists in the database before updating the entity
            UnitMeasure unit = unitMeasureRepository.findById(dto.unitId())
                                                 .orElseThrow(() -> new EntityNotFoundException("Unit of Measure not found with id: " + dto.unitId()));
            material.setMatUnit(unit);
        }

        return RawMaterialMapper.toDTO(rawMaterialRepository.save(material));
    }

      @Transactional
      public void delete(Integer id) {
        RawMaterial entity = rawMaterialRepository.findById(id)
                                                    .orElseThrow(() -> new EntityNotFoundException("Raw Material not found with id: " + id)); 

        rawMaterialRepository.delete(entity);                                                    
      }

    

}
