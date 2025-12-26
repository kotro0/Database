package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = getEventById(id);

        if (eventDetails.getName() != null) {
            event.setName(eventDetails.getName());
        }
        if (eventDetails.getDescription() != null) {
            event.setDescription(eventDetails.getDescription());
        }
        if (eventDetails.getStartDate() != null) {
            event.setStartDate(eventDetails.getStartDate());
        }
        if (eventDetails.getEndDate() != null) {
            event.setEndDate(eventDetails.getEndDate());
        }
        if (eventDetails.getLocation() != null) {
            event.setLocation(eventDetails.getLocation());
        }
        if (eventDetails.getMaxParticipants() != null) {
            event.setMaxParticipants(eventDetails.getMaxParticipants());
        }
        if (eventDetails.getStatus() != null) {
            event.setStatus(eventDetails.getStatus());
        }
        if (eventDetails.getRequiresApproval() != null) {
            event.setRequiresApproval(eventDetails.getRequiresApproval());
        }
        if (eventDetails.getRegistrationOpen() != null) {
            event.setRegistrationOpen(eventDetails.getRegistrationOpen());
        }

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        event.setDeleted(true);
        eventRepository.save(event);
    }

    // Бизнес-операции

    public List<Event> getEventsWithAvailableSpots() {
        return eventRepository.findEventsWithAvailableSpots();
    }

    public List<Event> getPublicEvents() {
        return eventRepository.findByRegistrationOpenTrue();
    }

    public Event updateEventStatus(Long id, String statusStr) {
        Event event = getEventById(id);

        try {
            Event.EventStatus status = Event.EventStatus.valueOf(statusStr.toUpperCase());
            event.setStatus(status);
            return eventRepository.save(event);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + statusStr);
        }
    }

    public Event duplicateEvent(Long id) {
        Event original = getEventById(id);

        Event duplicate = new Event();
        duplicate.setName("Copy of " + original.getName());
        duplicate.setDescription(original.getDescription());
        duplicate.setStartDate(original.getStartDate().plusMonths(1));
        duplicate.setEndDate(original.getEndDate().plusMonths(1));
        duplicate.setLocation(original.getLocation());
        duplicate.setMaxParticipants(original.getMaxParticipants());
        duplicate.setStatus(Event.EventStatus.PLANNED);
        duplicate.setRequiresApproval(original.getRequiresApproval());
        duplicate.setRegistrationOpen(true);
        duplicate.setDeleted(false);

        return eventRepository.save(duplicate);
    }
}