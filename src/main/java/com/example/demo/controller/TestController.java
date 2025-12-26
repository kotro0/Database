package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.Participant;
import com.example.demo.model.Registration;
import com.example.demo.model.Session;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // ДОБАВЬТЕ ЭТОТ ИМПОРТ!

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final SessionRepository sessionRepository;
    private final RegistrationService registrationService;

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/participants")
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/sessions")
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @PostMapping("/register/event")
    public ResponseEntity<?> registerForEvent(@RequestParam Long participantId,
                                              @RequestParam Long eventId) {
        try {
            Registration registration = registrationService.registerForEvent(participantId, eventId);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/session")
    public ResponseEntity<?> registerForSession(@RequestParam Long participantId,
                                                @RequestParam Long sessionId) {
        try {
            Registration registration = registrationService.registerForSession(participantId, sessionId);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel/{registrationId}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long registrationId) {
        try {
            registrationService.cancelRegistration(registrationId);
            return ResponseEntity.ok("Registration cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}