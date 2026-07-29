package com.campuswork.eventservice.service;

import com.campuswork.eventservice.dto.CreateEventRequest;
import com.campuswork.eventservice.dto.EventResponse;
import com.campuswork.eventservice.dto.RegistrationResponse;

import java.util.List;

public interface EventService {
    EventResponse createEvent(CreateEventRequest dto, Long creatorId);
    List<EventResponse> browseEvents();
    RegistrationResponse register(Long eventId, Long studentId);
    List<RegistrationResponse> getMyRegistrations(Long studentId);
    RegistrationResponse markAttended(Long registrationId, Long requesterId, boolean isAdmin);
}