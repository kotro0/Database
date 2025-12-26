package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Бизнес-операции
    @GetMapping("/available")
    public List<Event> getEventsWithAvailableSpots() {
        return eventService.getEventsWithAvailableSpots();
    }

    @GetMapping("/public")
    public List<Event> getPublicEvents() {
        return eventService.getPublicEvents();
    }

    @PatchMapping("/{id}/status")
    public Event updateEventStatus(@PathVariable Long id, @RequestParam String status) {
        return eventService.updateEventStatus(id, status);
    }

    @PostMapping("/{id}/duplicate")
    public Event duplicateEvent(@PathVariable Long id) {
        return eventService.duplicateEvent(id);
    }
}