package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final RegistrationService registrationService;
    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;

    // CRUD методы
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Session createSession(Session session) {
        if (session.getEvent() == null || session.getEvent().getId() == null) {
            throw new RuntimeException("Event is required");
        }

        Event event = eventRepository.findById(session.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        session.setEvent(event);

        if (session.getSpeaker() != null && session.getSpeaker().getId() != null) {
            Speaker speaker = speakerRepository.findById(session.getSpeaker().getId())
                    .orElseThrow(() -> new RuntimeException("Speaker not found"));
            session.setSpeaker(speaker);
        }

        return sessionRepository.save(session);
    }

    public Session updateSession(Long id, Session sessionDetails) {
        Session session = getSessionById(id);

        if (sessionDetails.getTitle() != null) {
            session.setTitle(sessionDetails.getTitle());
        }
        if (sessionDetails.getDescription() != null) {
            session.setDescription(sessionDetails.getDescription());
        }
        if (sessionDetails.getStartTime() != null) {
            session.setStartTime(sessionDetails.getStartTime());
        }
        if (sessionDetails.getEndTime() != null) {
            session.setEndTime(sessionDetails.getEndTime());
        }
        if (sessionDetails.getMaxCapacity() != null) {
            session.setMaxCapacity(sessionDetails.getMaxCapacity());
        }
        if (sessionDetails.getRoomNumber() != null) {
            session.setRoomNumber(sessionDetails.getRoomNumber());
        }
        if (sessionDetails.getType() != null) {
            session.setType(sessionDetails.getType());
        }
        if (sessionDetails.getStatus() != null) {
            session.setStatus(sessionDetails.getStatus());
        }
        if (sessionDetails.getRequiresRegistration() != null) {
            session.setRequiresRegistration(sessionDetails.getRequiresRegistration());
        }

        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        Session session = getSessionById(id);
        session.setStatus(Session.SessionStatus.CANCELLED);
        sessionRepository.save(session);
    }

    // Бизнес-операции

    // Исправленный метод (название должно совпадать с контроллером)
    public List<Session> getSessionsByEvent(Long eventId) {
        return sessionRepository.findByEventId(eventId);
    }

    // Алиас для совместимости с контроллером
    public List<Session> getSessionsByEventId(Long eventId) {
        return getSessionsByEvent(eventId);
    }

    public List<Session> getSessionsWithAvailableSeats() {
        return sessionRepository.findSessionsWithAvailableSeats();
    }

    // Алиас для совместимости с контроллером
    public List<Session> getAvailableSessions() {
        return getSessionsWithAvailableSeats();
    }

    // Этот метод не должен принимать параметров
    public List<Session> getUpcomingSessions() {
        return sessionRepository.findSessionsBetweenDates(
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );
    }

    public List<Session> getSessionsBySpeaker(Long speakerId) {
        return sessionRepository.findBySpeakerId(speakerId);
    }

    // Дополнительные методы для контроллера

    public boolean checkRoomAvailability(Long sessionId, LocalDateTime startTime, LocalDateTime endTime) {
        Session session = getSessionById(sessionId);

        if (session.getRoomNumber() == null) {
            return true; // Если комната не указана, считаем что доступна
        }

        // Ищем другие сессии в той же комнате в это же время
        List<Session> conflictingSessions = sessionRepository.findByEventId(session.getEvent().getId())
                .stream()
                .filter(s -> s.getRoomNumber() != null &&
                        s.getRoomNumber().equals(session.getRoomNumber()) &&
                        !s.getId().equals(sessionId) &&
                        s.getStartTime().isBefore(endTime) &&
                        s.getEndTime().isAfter(startTime))
                .collect(Collectors.toList());

        return conflictingSessions.isEmpty();
    }

    public Session updateSessionCapacity(Long sessionId, Integer newCapacity) {
        Session session = getSessionById(sessionId);

        if (newCapacity < session.getCurrentParticipants()) {
            throw new RuntimeException("New capacity cannot be less than current participants");
        }

        session.setMaxCapacity(newCapacity);
        return sessionRepository.save(session);
    }

    public Session duplicateSession(Long sessionId) {
        Session original = getSessionById(sessionId);

        Session duplicate = new Session();
        duplicate.setTitle("Copy of " + original.getTitle());
        duplicate.setDescription(original.getDescription());
        duplicate.setStartTime(original.getStartTime().plusDays(7));
        duplicate.setEndTime(original.getEndTime().plusDays(7));
        duplicate.setMaxCapacity(original.getMaxCapacity());
        duplicate.setRoomNumber(original.getRoomNumber());
        duplicate.setType(original.getType());
        duplicate.setStatus(Session.SessionStatus.SCHEDULED);
        duplicate.setRequiresRegistration(original.getRequiresRegistration());
        duplicate.setEvent(original.getEvent());
        duplicate.setSpeaker(original.getSpeaker());

        return sessionRepository.save(duplicate);
    }

    public List<Session> getFutureSessionsBySpeaker(Long speakerId) {
        List<Session> sessions = sessionRepository.findBySpeakerId(speakerId);

        return sessions.stream()
                .filter(s -> s.getStartTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public List<Participant> getSessionParticipants(Long sessionId) {
        List<Registration> registrations = registrationService.getRegistrationsBySession(sessionId);

        return registrations.stream()
                .map(Registration::getParticipant)
                .collect(Collectors.toList());
    }
}