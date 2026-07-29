package com.campuswork.eventservice.service.impl;

import com.campuswork.eventservice.dto.CreateEventRequest;
import com.campuswork.eventservice.dto.EventResponse;
import com.campuswork.eventservice.dto.RegistrationResponse;
import com.campuswork.eventservice.mapper.EventMapper;
import com.campuswork.eventservice.model.Event;
import com.campuswork.eventservice.model.EventRegistration;
import com.campuswork.eventservice.model.RegistrationStatus;
import com.campuswork.eventservice.repository.EventRegistrationRepository;
import com.campuswork.eventservice.repository.EventRepository;
import com.campuswork.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse createEvent(CreateEventRequest dto, Long creatorId) {
        Event entity = eventMapper.toEntity(dto, creatorId);
        Event saved = eventRepository.save(entity);
        return eventMapper.toResponse(saved, 0);
    }

    @Override
    public List<EventResponse> browseEvents() {
        return eventRepository.findAll().stream()
                .map(event -> eventMapper.toResponse(event, registrationRepository.countByEventId(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationResponse register(Long eventId, Long studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Event not found"));

        if (registrationRepository.findByEventIdAndStudentId(eventId, studentId).isPresent()) {
            throw new IllegalStateException("Already registered for this event");
        }

        if (event.getMaxParticipants() != null) {
            long current = registrationRepository.countByEventId(eventId);
            if (current >= event.getMaxParticipants()) {
                throw new IllegalStateException("Event is full");
            }
        }

        EventRegistration registration = EventRegistration.builder()
                .eventId(eventId)
                .studentId(studentId)
                .build();

        EventRegistration saved = registrationRepository.save(registration);
        return eventMapper.toRegistrationResponse(saved);
    }

    @Override
    public List<RegistrationResponse> getMyRegistrations(Long studentId) {
        return registrationRepository.findByStudentId(studentId).stream()
                .map(eventMapper::toRegistrationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationResponse markAttended(Long registrationId, Long requesterId, boolean isAdmin) {
        EventRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalStateException("Registration not found"));

        Event event = eventRepository.findById(registration.getEventId())
                .orElseThrow(() -> new IllegalStateException("Event not found"));

        if (!isAdmin && !event.getCreatedBy().equals(requesterId)) {
            throw new AccessDeniedException("You can only mark attendance for events you created");
        }

        registration.setStatus(RegistrationStatus.ATTENDED);
        EventRegistration updated = registrationRepository.save(registration);
        return eventMapper.toRegistrationResponse(updated);
    }
}