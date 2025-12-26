package com.example.demo.repository;

import com.example.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(Event.EventStatus status);

    List<Event> findByRegistrationOpenTrue();

    @Query("SELECT e FROM Event e WHERE e.startDate <= :date AND e.endDate >= :date")
    List<Event> findEventsByDate(@Param("date") LocalDateTime date);

    @Query("SELECT e FROM Event e WHERE e.maxParticipants IS NULL OR e.currentParticipants < e.maxParticipants")
    List<Event> findEventsWithAvailableSpots();

    List<Event> findByLocationContainingIgnoreCase(String location);
}