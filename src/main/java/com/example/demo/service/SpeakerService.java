package com.example.demo.service;

import com.example.demo.model.Session;
import com.example.demo.model.Speaker;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.SpeakerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;

    public SpeakerService(SpeakerRepository speakerRepository,
                          SessionRepository sessionRepository) {
        this.speakerRepository = speakerRepository;
        this.sessionRepository = sessionRepository;
    }

    public Speaker createSpeaker(Speaker speaker) {
        return speakerRepository.save(speaker);
    }

    public Speaker getSpeakerById(Long id) {
        return speakerRepository.findById(id).orElse(null);
    }

    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    public Speaker updateSpeaker(Long id, Speaker speakerDetails) {
        Speaker speaker = getSpeakerById(id);
        if (speaker != null) {
            speaker.setName(speakerDetails.getName());
            speaker.setEmail(speakerDetails.getEmail());
            speaker.setBio(speakerDetails.getBio());
            speaker.setCompany(speakerDetails.getCompany());
            speaker.setSpecialization(speakerDetails.getSpecialization());
            return speakerRepository.save(speaker);
        }
        return null;
    }

    public void deleteSpeaker(Long id) {
        speakerRepository.deleteById(id);
    }

    public List<Speaker> getFeaturedSpeakers() {
        return speakerRepository.findAll().stream()
                .filter(speaker -> speaker.getIsFeatured() != null &&
                        speaker.getIsFeatured())
                .toList();
    }

    public List<Speaker> getTopRatedSpeakers(Double minRating) {
        return speakerRepository.findAll().stream()
                .filter(speaker -> speaker.getAverageRating() != null &&
                        speaker.getAverageRating() >= minRating)
                .toList();
    }

    public List<Session> getSpeakerSessions(Long speakerId) {
        return sessionRepository.findBySpeakerId(speakerId);
    }

    public void rateSpeaker(Long speakerId, Long participantId, Integer rating, String comment) {
        Speaker speaker = getSpeakerById(speakerId);
        if (speaker != null) {
            speaker.addRating(rating);
            speakerRepository.save(speaker);
        }
    }

    public Speaker toggleFeaturedStatus(Long id) {
        Speaker speaker = getSpeakerById(id);
        if (speaker != null) {
            Boolean currentStatus = speaker.getIsFeatured();
            speaker.setIsFeatured(currentStatus == null || !currentStatus);
            return speakerRepository.save(speaker);
        }
        return null;
    }

    public List<Speaker> getAvailableSpeakers(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);

        return speakerRepository.findAll().stream()
                .filter(speaker -> isSpeakerAvailable(speaker, start, end))
                .toList();
    }

    private boolean isSpeakerAvailable(Speaker speaker, LocalDateTime start, LocalDateTime end) {
        List<Session> sessions = sessionRepository.findBySpeakerId(speaker.getId());
        return sessions.stream()
                .noneMatch(session -> isTimeOverlap(session, start, end));
    }

    private boolean isTimeOverlap(Session session, LocalDateTime start, LocalDateTime end) {
        if (session.getStartTime() == null || session.getEndTime() == null) {
            return false;
        }
        return session.getStartTime().isBefore(end) &&
                start.isBefore(session.getEndTime());
    }
}