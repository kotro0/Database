package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ParticipantRepository participantRepository;
    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;

    // CRUD операции
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public Registration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public void deleteRegistration(Long id) {
        Registration registration = getRegistrationById(id);
        registrationRepository.delete(registration);
    }

    // Бизнес-операции
    @Transactional
    public Registration registerForEvent(Long participantId, Long eventId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + participantId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        // Проверка, открыта ли регистрация
        if (!event.isRegistrationOpen()) {
            throw new RuntimeException("Registration for this event is closed");
        }

        // Проверка доступности мест
        if (!event.hasAvailableSpots()) {
            throw new RuntimeException("Event is full");
        }

        // Проверка, не зарегистрирован ли уже
        Registration existingRegistration = registrationRepository.findByParticipantIdAndEventId(participantId, eventId);
        if (existingRegistration != null &&
                (existingRegistration.getStatus() == Registration.RegistrationStatus.CONFIRMED ||
                        existingRegistration.getStatus() == Registration.RegistrationStatus.PENDING ||
                        existingRegistration.getStatus() == Registration.RegistrationStatus.WAITLISTED)) {
            throw new RuntimeException("Participant already registered for this event");
        }

        // Создание регистрации
        Registration registration = new Registration();
        registration.setParticipant(participant);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus(Registration.RegistrationStatus.CONFIRMED);

        // Увеличиваем счетчик участников
        event.incrementParticipants();
        eventRepository.save(event);

        return registrationRepository.save(registration);
    }

    @Transactional
    public Registration registerForSession(Long participantId, Long sessionId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + participantId));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));

        Event event = session.getEvent();

        // Проверка основных условий
        if (!event.isRegistrationOpen()) {
            throw new RuntimeException("Registration for this event is closed");
        }

        // Проверка доступности мест в сессии
        long currentRegistrations = registrationRepository.countBySessionId(sessionId);
        if (session.getMaxCapacity() != null && currentRegistrations >= session.getMaxCapacity()) {
            throw new RuntimeException("Session is full");
        }

        // Проверка, не зарегистрирован ли уже на эту сессию
        if (registrationRepository.existsByParticipantIdAndSessionId(participantId, sessionId)) {
            throw new RuntimeException("Participant already registered for this session");
        }

        // Проверка пересечения времени
        boolean hasTimeConflict = registrationRepository.existsParticipantTimeConflict(
                participantId,
                session.getStartTime(),
                session.getEndTime()
        );

        if (hasTimeConflict) {
            throw new RuntimeException("Participant has time conflict with another session");
        }

        // Проверка, зарегистрирован ли на событие
        Registration eventRegistration = registrationRepository.findByParticipantIdAndEventId(participantId, event.getId());
        if (eventRegistration == null ||
                eventRegistration.getStatus() == Registration.RegistrationStatus.CANCELLED) {
            // Если не зарегистрирован на событие, создаем регистрацию
            eventRegistration = registerForEvent(participantId, event.getId());
        }

        // Создание регистрации на сессию
        Registration registration = new Registration();
        registration.setParticipant(participant);
        registration.setSession(session);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus(Registration.RegistrationStatus.CONFIRMED);

        // Увеличиваем счетчик участников сессии
        session.incrementParticipants();
        sessionRepository.save(session);

        return registrationRepository.save(registration);
    }

    // Алиас для совместимости с контроллером
    @Transactional
    public Registration registerParticipantToSession(Long participantId, Long sessionId) {
        return registerForSession(participantId, sessionId);
    }

    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (registration.getStatus() == Registration.RegistrationStatus.CANCELLED) {
            throw new RuntimeException("Registration already cancelled");
        }

        // Сохраняем предыдущий статус
        registration.setStatus(Registration.RegistrationStatus.CANCELLED);

        // Если это регистрация на событие, уменьшаем счетчик события
        if (registration.getSession() == null && registration.getEvent() != null) {
            Event event = registration.getEvent();
            event.decrementParticipants();
            eventRepository.save(event);
        }

        // Если это регистрация на сессию, уменьшаем счетчик сессии
        if (registration.getSession() != null) {
            Session session = registration.getSession();
            session.decrementParticipants();
            sessionRepository.save(session);
        }

        registrationRepository.save(registration);
    }

    // Дополнительные методы

    public List<Registration> getRegistrationsBySession(Long sessionId) {
        return registrationRepository.findBySessionId(sessionId);
    }

    public List<Registration> getSessionParticipants(Long sessionId) {
        return registrationRepository.findBySessionId(sessionId);
    }

    public List<Registration> getParticipantRegistrations(Long participantId) {
        return registrationRepository.findByParticipantId(participantId);
    }

    @Transactional
    public Registration moveToWaitlist(Long registrationId) {
        Registration registration = getRegistrationById(registrationId);
        registration.setStatus(Registration.RegistrationStatus.WAITLISTED);
        return registrationRepository.save(registration);
    }

    @Transactional
    public Registration confirmAttendance(Long registrationId) {
        Registration registration = getRegistrationById(registrationId);
        registration.setStatus(Registration.RegistrationStatus.ATTENDED);
        return registrationRepository.save(registration);
    }
}