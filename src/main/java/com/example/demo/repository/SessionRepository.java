package com.example.demo.repository;

import com.example.demo.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByEventId(Long eventId);

    List<Session> findBySpeakerId(Long speakerId);

    @Query("SELECT s FROM Session s WHERE s.startTime >= :start AND s.endTime <= :end")
    List<Session> findSessionsBetweenDates(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Session s WHERE s.maxCapacity IS NULL OR s.currentParticipants < s.maxCapacity")
    List<Session> findSessionsWithAvailableSeats();

    @Query("SELECT s FROM Session s WHERE s.event.id = :eventId AND s.startTime >= :startTime AND s.endTime <= :endTime")
    List<Session> findByEventIdAndTimeRange(@Param("eventId") Long eventId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
}