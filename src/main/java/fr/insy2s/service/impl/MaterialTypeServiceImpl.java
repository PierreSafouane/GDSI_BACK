package fr.insy2s.service.impl;

import fr.insy2s.service.MaterialTypeService;
import fr.insy2s.domain.MaterialType;
import fr.insy2s.repository.MaterialTypeRepository;
import fr.insy2s.service.dto.MaterialTypeDTO;
import fr.insy2s.service.mapper.MaterialTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MaterialType}.
 */
@Service
@Transactional
public class MaterialTypeServiceImpl implements MaterialTypeService {

    private final Logger log = LoggerFactory.getLogger(MaterialTypeServiceImpl.class);

    private final MaterialTypeRepository materialTypeRepository;

    private final MaterialTypeMapper materialTypeMapper;

    public MaterialTypeServiceImpl(MaterialTypeRepository materialTypeRepository, MaterialTypeMapper materialTypeMapper) {
        this.materialTypeRepository = materialTypeRepository;
        this.materialTypeMapper = materialTypeMapper;
    }

    @Override
    public MaterialTypeDTO save(MaterialTypeDTO materialTypeDTO) {
        log.debug("Request to save MaterialType : {}", materialTypeDTO);
        MaterialType materialType = materialTypeMapper.toEntity(materialTypeDTO);
        materialType = materialTypeRepository.save(materialType);
        return materialTypeMapper.toDto(materialType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialTypeDTO> findAll() {
        log.debug("Request to get all MaterialTypes");
        return materialTypeRepository.findAll().stream()
            .map(materialTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialTypeDTO> findOne(Long id) {
        log.debug("Request to get MaterialType : {}", id);
        return materialTypeRepository.findById(id)
            .map(materialTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialType : {}", id);
        materialTypeRepository.deleteById(id);
    }
}
