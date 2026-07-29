package com.campuswork.eventservice.repository;

import com.campuswork.eventservice.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByStudentId(Long studentId);
    List<EventRegistration> findByEventId(Long eventId);
    Optional<EventRegistration> findByEventIdAndStudentId(Long eventId, Long studentId);
    long countByEventId(Long eventId);
}