package fr.insy2s.service.impl;

import fr.insy2s.service.RoomImageService;
import fr.insy2s.domain.RoomImage;
import fr.insy2s.repository.RoomImageRepository;
import fr.insy2s.service.dto.RoomImageDTO;
import fr.insy2s.service.mapper.RoomImageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RoomImage}.
 */
@Service
@Transactional
public class RoomImageServiceImpl implements RoomImageService {

    private final Logger log = LoggerFactory.getLogger(RoomImageServiceImpl.class);

    private final RoomImageRepository roomImageRepository;

    private final RoomImageMapper roomImageMapper;

    public RoomImageServiceImpl(RoomImageRepository roomImageRepository, RoomImageMapper roomImageMapper) {
        this.roomImageRepository = roomImageRepository;
        this.roomImageMapper = roomImageMapper;
    }

    @Override
    public RoomImageDTO save(RoomImageDTO roomImageDTO) {
        log.debug("Request to save RoomImage : {}", roomImageDTO);
        RoomImage roomImage = roomImageMapper.toEntity(roomImageDTO);
        roomImage = roomImageRepository.save(roomImage);
        return roomImageMapper.toDto(roomImage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomImageDTO> findAll() {
        log.debug("Request to get all RoomImages");
        return roomImageRepository.findAll().stream()
            .map(roomImageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RoomImageDTO> findOne(Long id) {
        log.debug("Request to get RoomImage : {}", id);
        return roomImageRepository.findById(id)
            .map(roomImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomImage : {}", id);
        roomImageRepository.deleteById(id);
    }
}
