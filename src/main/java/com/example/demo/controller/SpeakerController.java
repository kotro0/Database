package com.example.demo.controller;

import com.example.demo.model.Session;
import com.example.demo.model.Speaker;
import com.example.demo.service.SpeakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speakers")
public class SpeakerController {
    private final SpeakerService speakerService;

    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    // CRUD операции
    @PostMapping
    public ResponseEntity<Speaker> createSpeaker(@RequestBody Speaker speaker) {
        Speaker createdSpeaker = speakerService.createSpeaker(speaker);
        return new ResponseEntity<>(createdSpeaker, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Speaker> getSpeaker(@PathVariable Long id) {
        Speaker speaker = speakerService.getSpeakerById(id);
        if (speaker == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(speaker);
    }

    @GetMapping
    public ResponseEntity<List<Speaker>> getAllSpeakers() {
        List<Speaker> speakers = speakerService.getAllSpeakers();
        return ResponseEntity.ok(speakers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Speaker> updateSpeaker(
            @PathVariable Long id,
            @RequestBody Speaker speaker) {
        Speaker updatedSpeaker = speakerService.updateSpeaker(id, speaker);
        if (updatedSpeaker == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedSpeaker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }

    // БИЗНЕС-ОПЕРАЦИИ
    @GetMapping("/featured")
    public ResponseEntity<List<Speaker>> getFeaturedSpeakers() {
        List<Speaker> speakers = speakerService.getFeaturedSpeakers();
        return ResponseEntity.ok(speakers);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Speaker>> getTopRatedSpeakers(
            @RequestParam(defaultValue = "4.0") Double minRating) {
        List<Speaker> speakers = speakerService.getTopRatedSpeakers(minRating);
        return ResponseEntity.ok(speakers);
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<Session>> getSpeakerSessions(@PathVariable Long id) {
        List<Session> sessions = speakerService.getSpeakerSessions(id);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateSpeaker(
            @PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment,
            @RequestParam Long participantId) {

        speakerService.rateSpeaker(id, participantId, rating, comment);
        return ResponseEntity.ok("Спасибо за вашу оценку!");
    }

    @PatchMapping("/{id}/featured")
    public ResponseEntity<Speaker> toggleFeaturedStatus(@PathVariable Long id) {
        Speaker speaker = speakerService.toggleFeaturedStatus(id);
        if (speaker == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(speaker);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Speaker>> getAvailableSpeakers(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        List<Speaker> speakers = speakerService.getAvailableSpeakers(startTime, endTime);
        return ResponseEntity.ok(speakers);
    }
}