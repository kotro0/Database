package com.example.demo.controller;

import com.example.demo.model.Session;
import com.example.demo.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }

    @PutMapping("/{id}")
    public Session updateSession(@PathVariable Long id, @RequestBody Session session) {
        return sessionService.updateSession(id, session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    // Бизнес-операции
    @GetMapping("/event/{eventId}")
    public List<Session> getSessionsByEvent(@PathVariable Long eventId) {
        return sessionService.getSessionsByEvent(eventId);
    }

    @GetMapping("/available")
    public List<Session> getAvailableSessions() {
        return sessionService.getAvailableSessions();
    }

    @GetMapping("/upcoming")
    public List<Session> getUpcomingSessions() {
        return sessionService.getUpcomingSessions();
    }

    @GetMapping("/speaker/{speakerId}")
    public List<Session> getSessionsBySpeaker(@PathVariable Long speakerId) {
        return sessionService.getSessionsBySpeaker(speakerId);
    }

    @GetMapping("/{id}/room-availability")
    public ResponseEntity<Boolean> checkRoomAvailability(@PathVariable Long id,
                                                         @RequestParam LocalDateTime startTime,
                                                         @RequestParam LocalDateTime endTime) {
        boolean isAvailable = sessionService.checkRoomAvailability(id, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }

    @PatchMapping("/{id}/capacity")
    public Session updateSessionCapacity(@PathVariable Long id, @RequestParam Integer capacity) {
        return sessionService.updateSessionCapacity(id, capacity);
    }

    @PostMapping("/{id}/duplicate")
    public Session duplicateSession(@PathVariable Long id) {
        return sessionService.duplicateSession(id);
    }

    @GetMapping("/speaker/{speakerId}/future")
    public List<Session> getFutureSessionsBySpeaker(@PathVariable Long speakerId) {
        return sessionService.getFutureSessionsBySpeaker(speakerId);
    }

    @GetMapping("/{id}/participants")
    public List<com.example.demo.model.Participant> getSessionParticipants(@PathVariable Long id) {
        return sessionService.getSessionParticipants(id);
    }
}