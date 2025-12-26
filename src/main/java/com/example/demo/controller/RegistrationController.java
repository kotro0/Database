package com.example.demo.controller;

import com.example.demo.model.Registration;
import com.example.demo.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

    // Бизнес-операции
    @PostMapping("/session")
    public Registration registerParticipantToSession(@RequestParam Long participantId,
                                                     @RequestParam Long sessionId) {
        return registrationService.registerParticipantToSession(participantId, sessionId);
    }

    @PostMapping("/event")
    public Registration registerParticipantToEvent(@RequestParam Long participantId,
                                                   @RequestParam Long eventId) {
        return registrationService.registerForEvent(participantId, eventId);
    }

    @GetMapping("/session/{sessionId}")
    public List<Registration> getRegistrationsBySession(@PathVariable Long sessionId) {
        return registrationService.getRegistrationsBySession(sessionId);
    }

    @GetMapping("/participant/{participantId}")
    public List<Registration> getRegistrationsByParticipant(@PathVariable Long participantId) {
        return registrationService.getParticipantRegistrations(participantId);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long id) {
        try {
            registrationService.cancelRegistration(id);
            return ResponseEntity.ok("Registration cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/waitlist")
    public Registration moveToWaitlist(@PathVariable Long id) {
        return registrationService.moveToWaitlist(id);
    }

    @PostMapping("/{id}/attend")
    public Registration confirmAttendance(@PathVariable Long id) {
        return registrationService.confirmAttendance(id);
    }
}