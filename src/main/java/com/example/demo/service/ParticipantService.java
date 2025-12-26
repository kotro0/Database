package com.example.demo.service;

import com.example.demo.model.Participant;
import com.example.demo.model.Registration;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final RegistrationRepository registrationRepository;

    // CRUD методы
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipantById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }

    public Participant createParticipant(Participant participant) {
        Optional<Participant> existing = participantRepository.findByEmail(participant.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Participant with this email already exists");
        }

        return participantRepository.save(participant);
    }

    public Participant updateParticipant(Long id, Participant participantDetails) {
        Participant participant = getParticipantById(id);

        if (participantDetails.getFirstName() != null) {
            participant.setFirstName(participantDetails.getFirstName());
        }
        if (participantDetails.getLastName() != null) {
            participant.setLastName(participantDetails.getLastName());
        }

        if (participantDetails.getEmail() != null &&
                !participantDetails.getEmail().equals(participant.getEmail())) {
            Optional<Participant> existing = participantRepository.findByEmail(participantDetails.getEmail());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new RuntimeException("Email already in use by another participant");
            }
            participant.setEmail(participantDetails.getEmail());
        }

        if (participantDetails.getPhone() != null) {
            participant.setPhone(participantDetails.getPhone());
        }
        if (participantDetails.getCompany() != null) {
            participant.setCompany(participantDetails.getCompany());
        }
        if (participantDetails.getPosition() != null) {
            participant.setPosition(participantDetails.getPosition());
        }
        if (participantDetails.getIsActive() != null) {
            participant.setIsActive(participantDetails.getIsActive());
        }

        return participantRepository.save(participant);
    }

    public void deleteParticipant(Long id) {
        Participant participant = getParticipantById(id);
        participant.setIsActive(false);
        participantRepository.save(participant);
    }

    // Бизнес-операции

    public List<Participant> getParticipantsByCompany(String company) {
        return participantRepository.findByCompany(company);
    }

    public List<Participant> getActiveParticipants() {
        return participantRepository.findByIsActiveTrue();
    }

    public Participant deactivateParticipant(Long id) {
        Participant participant = getParticipantById(id);
        participant.setIsActive(false);
        return participantRepository.save(participant);
    }

    public Participant activateParticipant(Long id) {
        Participant participant = getParticipantById(id);
        participant.setIsActive(true);
        return participantRepository.save(participant);
    }

    // Дополнительные методы для контроллера

    public List<Registration> getParticipantRegistrations(Long participantId) {
        return registrationRepository.findByParticipantId(participantId);
    }

    public List<Participant> getParticipantsByEvent(Long eventId) {
        List<Registration> registrations = registrationRepository.findByEventId(eventId);

        return registrations.stream()
                .map(Registration::getParticipant)
                .distinct()
                .collect(Collectors.toList());
    }

    // ИСПРАВЛЕННЫЙ МЕТОД - был баг с передачей параметра
    public Participant verifyParticipant(Long participantId) {
        // В данном контексте просто активируем участника
        return activateParticipant(participantId);
    }

    public List<Participant> searchParticipants(String keyword) {
        List<Participant> allParticipants = participantRepository.findAll();

        return allParticipants.stream()
                .filter(p ->
                        (p.getFirstName() != null && p.getFirstName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (p.getLastName() != null && p.getLastName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (p.getEmail() != null && p.getEmail().toLowerCase().contains(keyword.toLowerCase())) ||
                                (p.getCompany() != null && p.getCompany().toLowerCase().contains(keyword.toLowerCase()))
                )
                .collect(Collectors.toList());
    }
}