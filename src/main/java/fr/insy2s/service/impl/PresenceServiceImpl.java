package fr.insy2s.service.impl;

import fr.insy2s.service.PresenceService;
import fr.insy2s.domain.Presence;
import fr.insy2s.repository.PresenceRepository;
import fr.insy2s.service.dto.PresenceDTO;
import fr.insy2s.service.mapper.PresenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Presence}.
 */
@Service
@Transactional
public class PresenceServiceImpl implements PresenceService {

    private final Logger log = LoggerFactory.getLogger(PresenceServiceImpl.class);

    private final PresenceRepository presenceRepository;

    private final PresenceMapper presenceMapper;

    public PresenceServiceImpl(PresenceRepository presenceRepository, PresenceMapper presenceMapper) {
        this.presenceRepository = presenceRepository;
        this.presenceMapper = presenceMapper;
    }

    @Override
    public PresenceDTO save(PresenceDTO presenceDTO) {
        log.debug("Request to save Presence : {}", presenceDTO);
        Presence presence = presenceMapper.toEntity(presenceDTO);
        presence = presenceRepository.save(presence);
        return presenceMapper.toDto(presence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresenceDTO> findAll() {
        log.debug("Request to get all Presences");
        return presenceRepository.findAll().stream()
            .map(presenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PresenceDTO> findOne(Long id) {
        log.debug("Request to get Presence : {}", id);
        return presenceRepository.findById(id)
            .map(presenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Presence : {}", id);
        presenceRepository.deleteById(id);
    }
}
