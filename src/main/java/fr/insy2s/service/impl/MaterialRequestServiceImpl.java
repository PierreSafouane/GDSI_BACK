package fr.insy2s.service.impl;

import fr.insy2s.service.MaterialRequestService;
import fr.insy2s.domain.MaterialRequest;
import fr.insy2s.repository.MaterialRequestRepository;
import fr.insy2s.service.dto.MaterialRequestDTO;
import fr.insy2s.service.mapper.MaterialRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MaterialRequest}.
 */
@Service
@Transactional
public class MaterialRequestServiceImpl implements MaterialRequestService {

    private final Logger log = LoggerFactory.getLogger(MaterialRequestServiceImpl.class);

    private final MaterialRequestRepository materialRequestRepository;

    private final MaterialRequestMapper materialRequestMapper;

    public MaterialRequestServiceImpl(MaterialRequestRepository materialRequestRepository, MaterialRequestMapper materialRequestMapper) {
        this.materialRequestRepository = materialRequestRepository;
        this.materialRequestMapper = materialRequestMapper;
    }

    @Override
    public MaterialRequestDTO save(MaterialRequestDTO materialRequestDTO) {
        log.debug("Request to save MaterialRequest : {}", materialRequestDTO);
        MaterialRequest materialRequest = materialRequestMapper.toEntity(materialRequestDTO);
        materialRequest = materialRequestRepository.save(materialRequest);
        return materialRequestMapper.toDto(materialRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialRequests");
        return materialRequestRepository.findAll(pageable)
            .map(materialRequestMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialRequestDTO> findOne(Long id) {
        log.debug("Request to get MaterialRequest : {}", id);
        return materialRequestRepository.findById(id)
            .map(materialRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialRequest : {}", id);
        materialRequestRepository.deleteById(id);
    }
}
