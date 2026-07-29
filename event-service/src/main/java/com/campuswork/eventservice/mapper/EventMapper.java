package com.campuswork.eventservice.mapper;

import com.campuswork.eventservice.dto.CreateEventRequest;
import com.campuswork.eventservice.dto.EventResponse;
import com.campuswork.eventservice.dto.RegistrationResponse;
import com.campuswork.eventservice.model.Event;
import com.campuswork.eventservice.model.EventRegistration;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(CreateEventRequest dto, Long createdBy) {
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation())
                .maxParticipants(dto.getMaxParticipants())
                .createdBy(createdBy)
                .build();
    }

    public EventResponse toResponse(Event entity, long registeredCount) {
        return EventResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .eventDate(entity.getEventDate())
                .location(entity.getLocation())
                .createdBy(entity.getCreatedBy())
                .maxParticipants(entity.getMaxParticipants())
                .registeredCount(registeredCount)
                .build();
    }

    public RegistrationResponse toRegistrationResponse(EventRegistration entity) {
        return RegistrationResponse.builder()
                .id(entity.getId())
                .eventId(entity.getEventId())
                .studentId(entity.getStudentId())
                .status(entity.getStatus())
                .registeredAt(entity.getRegisteredAt())
                .build();
    }
}