package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    public enum EventStatus {
        PLANNED, ACTIVE, COMPLETED, CANCELLED, POSTPONED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String location;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.PLANNED;

    @Column(name = "requires_approval", nullable = false)
    private Boolean requiresApproval = false;

    @Column(name = "registration_open")
    private Boolean registrationOpen = true;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registration> registrations = new ArrayList<>();

    // Методы бизнес-логики
    public void incrementParticipants() {
        if (this.currentParticipants == null) {
            this.currentParticipants = 0;
        }
        this.currentParticipants++;
    }

    public void decrementParticipants() {
        if (this.currentParticipants != null && this.currentParticipants > 0) {
            this.currentParticipants--;
        }
    }

    public Boolean isRegistrationOpen() {
        return registrationOpen != null ? registrationOpen : true;
    }

    public Boolean hasAvailableSpots() {
        if (maxParticipants == null) return true;
        if (currentParticipants == null) return true;
        return currentParticipants < maxParticipants;
    }

    // Builder method (для DataInitializer)
    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public static class EventBuilder {
        private String name;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String location;
        private Integer maxParticipants;
        private EventStatus status = EventStatus.PLANNED;
        private Boolean requiresApproval = false;
        private Boolean registrationOpen = true;

        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public EventBuilder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public EventBuilder location(String location) {
            this.location = location;
            return this;
        }

        public EventBuilder maxParticipants(Integer maxParticipants) {
            this.maxParticipants = maxParticipants;
            return this;
        }

        public EventBuilder status(EventStatus status) {
            this.status = status;
            return this;
        }

        public EventBuilder requiresApproval(Boolean requiresApproval) {
            this.requiresApproval = requiresApproval;
            return this;
        }

        public EventBuilder registrationOpen(Boolean registrationOpen) {
            this.registrationOpen = registrationOpen;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setName(name);
            event.setDescription(description);
            event.setStartDate(startDate);
            event.setEndDate(endDate);
            event.setLocation(location);
            event.setMaxParticipants(maxParticipants);
            event.setStatus(status);
            event.setRequiresApproval(requiresApproval);
            event.setRegistrationOpen(registrationOpen);
            event.setDeleted(false);
            return event;
        }
    }
}