package fr.insy2s.service.impl;

import fr.insy2s.service.BookingService;
import fr.insy2s.service.PresenceService;
import fr.insy2s.domain.Presence;
import fr.insy2s.repository.PresenceRepository;
import fr.insy2s.service.UserService;
import fr.insy2s.service.dto.BookingDTO;
import fr.insy2s.service.dto.PresenceDTO;
import fr.insy2s.service.dto.UserDTO;
import fr.insy2s.service.mapper.BookingMapper;
import fr.insy2s.service.mapper.PresenceMapper;
import fr.insy2s.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final BookingService bookingService;

    private final UserService userService;

    private final PresenceRepository presenceRepository;

    private final PresenceMapper presenceMapper;

    private final BookingMapper bookingMapper;

    private final UserMapper userMapper;

    public PresenceServiceImpl(BookingService bookingService, UserService userService, PresenceRepository presenceRepository, PresenceMapper presenceMapper, BookingMapper bookingMapper, UserMapper userMapper) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.presenceRepository = presenceRepository;
        this.presenceMapper = presenceMapper;
        this.bookingMapper = bookingMapper;
        this.userMapper = userMapper;
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

    @Override
    public List<BookingDTO> findBookingByUser(Long id) {
        log.debug("Request to get Booking by User in Presence table");
        List<Presence> presences = presenceRepository.findByAppUserId(id);
        List<Long> bookingsIds = new ArrayList<Long>();
        for (int i = 0; i < presences.size(); i++){
            bookingsIds.add(presences.get(i).getBooking().getId());
        }

        return bookingService.findBookingsByIdUser(bookingsIds).stream()
            .map(bookingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<UserDTO> findUserByBooking(Long id) {
        log.debug("Request to get User by Booking in Presence table");
        List<Presence> presences = presenceRepository.findByBookingId(id);
        List<Long> usersIds = new ArrayList<Long>();
        for (int i = 0; i < presences.size(); i++){
            usersIds.add(presences.get(i).getAppUser().getId());
        }
        return userMapper.usersToUserDTOs(userService.findUsersByIdBooking(usersIds));
    }
}
