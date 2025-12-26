package com.example.demo.repository;

import com.example.demo.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Метод для проверки существования регистрации
    boolean existsByParticipantIdAndSessionId(Long participantId, Long sessionId);

    // Метод для подсчета регистраций на сессию
    long countBySessionId(Long sessionId);

    // Метод для поиска регистрации по участнику и событию
    Registration findByParticipantIdAndEventId(Long participantId, Long eventId);

    // Метод для проверки пересечения времени
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Registration r " +
            "JOIN r.session s " +
            "WHERE r.participant.id = :participantId " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime")
    boolean existsParticipantTimeConflict(@Param("participantId") Long participantId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    // Дополнительные методы для удобства
    List<Registration> findByParticipantId(Long participantId);
    List<Registration> findBySessionId(Long sessionId);
    List<Registration> findByEventId(Long eventId);
    List<Registration> findByParticipantIdAndStatus(Long participantId, Registration.RegistrationStatus status);
}