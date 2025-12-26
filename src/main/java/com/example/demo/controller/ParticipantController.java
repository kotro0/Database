package com.example.demo.controller;

import com.example.demo.model.Participant;
import com.example.demo.model.Registration;
import com.example.demo.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.getParticipantById(id));
    }

    @PostMapping
    public Participant createParticipant(@RequestBody Participant participant) {
        return participantService.createParticipant(participant);
    }

    @PutMapping("/{id}")
    public Participant updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        return participantService.updateParticipant(id, participant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    // Бизнес-операции
    @GetMapping("/company/{company}")
    public List<Participant> getParticipantsByCompany(@PathVariable String company) {
        return participantService.getParticipantsByCompany(company);
    }

    @GetMapping("/active")
    public List<Participant> getActiveParticipants() {
        return participantService.getActiveParticipants();
    }

    @PostMapping("/{id}/deactivate")
    public Participant deactivateParticipant(@PathVariable Long id) {
        return participantService.deactivateParticipant(id);
    }

    @PostMapping("/{id}/activate")
    public Participant activateParticipant(@PathVariable Long id) {
        return participantService.activateParticipant(id);
    }

    @GetMapping("/{id}/registrations")
    public List<Registration> getParticipantRegistrations(@PathVariable Long id) {
        return participantService.getParticipantRegistrations(id);
    }

    @GetMapping("/event/{eventId}")
    public List<Participant> getParticipantsByEvent(@PathVariable Long eventId) {
        return participantService.getParticipantsByEvent(eventId);
    }

    @PostMapping("/{id}/verify")
    public Participant verifyParticipant(@PathVariable Long id) {
        return participantService.verifyParticipant(id);
    }

    @GetMapping("/search")
    public List<Participant> searchParticipants(@RequestParam String keyword) {
        return participantService.searchParticipants(keyword);
    }
}
// УБЕРИТЕ ЭТУ СКОБКУ ↓