package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {

    public enum SessionType {
        LECTURE, WORKSHOP, PANEL_DISCUSSION, NETWORKING, BREAK, KEYNOTE
    }

    public enum SessionStatus {
        SCHEDULED, ACTIVE, COMPLETED, CANCELLED, POSTPONED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity = 50;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Column(name = "room_number")
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SessionType type = SessionType.LECTURE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessionStatus status = SessionStatus.SCHEDULED;

    @Column(name = "requires_registration")
    private Boolean requiresRegistration = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id")
    private Speaker speaker;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Registration> registrations = new ArrayList<>();

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }

    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public SessionType getType() { return type; }
    public void setType(SessionType type) { this.type = type; }

    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }

    public Boolean getRequiresRegistration() { return requiresRegistration; }
    public void setRequiresRegistration(Boolean requiresRegistration) { this.requiresRegistration = requiresRegistration; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Speaker getSpeaker() { return speaker; }
    public void setSpeaker(Speaker speaker) { this.speaker = speaker; }

    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }

    // Бизнес-методы
    public boolean hasAvailableSeats() {
        if (maxCapacity == null || currentParticipants == null) {
            return true;
        }
        return currentParticipants < maxCapacity;
    }

    public boolean isSessionOver() {
        LocalDateTime now = LocalDateTime.now();
        if (endTime == null) {
            return false;
        }
        return now.isAfter(endTime) ||
                status == SessionStatus.COMPLETED ||
                status == SessionStatus.CANCELLED;
    }

    public void incrementParticipants() {
        if (this.currentParticipants == null) {
            this.currentParticipants = 0;
        }
        if (this.currentParticipants < this.maxCapacity) {
            this.currentParticipants++;
        }
    }

    public void decrementParticipants() {
        if (this.currentParticipants != null && this.currentParticipants > 0) {
            this.currentParticipants--;
        }
    }

    public boolean overlapsWith(Session other) {
        if (other == null || startTime == null || endTime == null ||
                other.startTime == null || other.endTime == null) {
            return false;
        }
        return this.startTime.isBefore(other.endTime) &&
                other.startTime.isBefore(this.endTime);
    }

    // Builder для DataInitializer
    public static SessionBuilder builder() {
        return new SessionBuilder();
    }

    public static class SessionBuilder {
        private String title;
        private String description;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer maxCapacity = 50;
        private Integer currentParticipants = 0;
        private String roomNumber;
        private SessionType type = SessionType.LECTURE;
        private SessionStatus status = SessionStatus.SCHEDULED;
        private Boolean requiresRegistration = true;
        private Event event;
        private Speaker speaker;

        public SessionBuilder title(String title) {
            this.title = title;
            return this;
        }

        public SessionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public SessionBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public SessionBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public SessionBuilder maxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public SessionBuilder currentParticipants(Integer currentParticipants) {
            this.currentParticipants = currentParticipants;
            return this;
        }

        public SessionBuilder roomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public SessionBuilder type(SessionType type) {
            this.type = type;
            return this;
        }

        public SessionBuilder status(SessionStatus status) {
            this.status = status;
            return this;
        }

        public SessionBuilder requiresRegistration(Boolean requiresRegistration) {
            this.requiresRegistration = requiresRegistration;
            return this;
        }

        public SessionBuilder event(Event event) {
            this.event = event;
            return this;
        }

        public SessionBuilder speaker(Speaker speaker) {
            this.speaker = speaker;
            return this;
        }

        public Session build() {
            Session session = new Session();
            session.setTitle(title);
            session.setDescription(description);
            session.setStartTime(startTime);
            session.setEndTime(endTime);
            session.setMaxCapacity(maxCapacity);
            session.setCurrentParticipants(currentParticipants);
            session.setRoomNumber(roomNumber);
            session.setType(type);
            session.setStatus(status);
            session.setRequiresRegistration(requiresRegistration);
            session.setEvent(event);
            session.setSpeaker(speaker);
            return session;
        }
    }
}