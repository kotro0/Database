package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final ParticipantRepository participantRepository;
    private final SessionRepository sessionRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public void run(String... args) {
        log.info("Starting data initialization...");

        try {
            // Очистка данных
            registrationRepository.deleteAll();
            sessionRepository.deleteAll();
            eventRepository.deleteAll();
            participantRepository.deleteAll();
            speakerRepository.deleteAll();

            log.info("Creating events...");

            // Создание событий с использованием Builder
            Event javaConference = Event.builder()
                    .name("Java Conference 2024")
                    .description("Главная конференция для Java разработчиков")
                    .startDate(LocalDateTime.of(2024, 6, 15, 9, 0))
                    .endDate(LocalDateTime.of(2024, 6, 17, 18, 0))
                    .location("Москва, Крокус Экспо")
                    .maxParticipants(500)
                    .status(Event.EventStatus.PLANNED)
                    .requiresApproval(false)
                    .registrationOpen(true)
                    .build();

            Event springWorkshop = Event.builder()
                    .name("Spring Boot Workshop")
                    .description("Практический воркшоп по Spring Boot")
                    .startDate(LocalDateTime.of(2024, 6, 20, 10, 0))
                    .endDate(LocalDateTime.of(2024, 6, 20, 16, 0))
                    .location("Онлайн")
                    .maxParticipants(50)
                    .status(Event.EventStatus.PLANNED)
                    .requiresApproval(false)
                    .registrationOpen(true)
                    .build();

            Event microservicesConference = Event.builder()
                    .name("Microservices Architecture Conference")
                    .description("Конференция по микросервисной архитектуре")
                    .startDate(LocalDateTime.of(2024, 7, 10, 9, 0))
                    .endDate(LocalDateTime.of(2024, 7, 11, 17, 0))
                    .location("Санкт-Петербург, Экспофорум")
                    .maxParticipants(300)
                    .status(Event.EventStatus.PLANNED)
                    .requiresApproval(false)
                    .registrationOpen(true)
                    .build();

            eventRepository.saveAll(List.of(javaConference, springWorkshop, microservicesConference));
            log.info("Created {} events", eventRepository.count());

            log.info("Creating speakers...");

            // Создание спикеров - обычное создание объектов
            Speaker speaker1 = new Speaker();
            speaker1.setName("Иван Петров");
            speaker1.setEmail("ivan.petrov@example.com");
            speaker1.setBio("Senior Java Developer с 10-летним опытом, эксперт по Spring Framework");
            speaker1.setCompany("TechCorp");
            speaker1.setSpecialization("Java, Spring, Microservices");
            speaker1.setPhoneNumber("+7 (999) 123-45-67");
            speaker1.setSpeakerLevel(Speaker.SpeakerLevel.EXPERT);
            speaker1.setIsFeatured(true);
            speaker1.setAverageRating(4.8);
            speaker1.setTotalRatings(25);
            speaker1.setDeleted(false);

            Speaker speaker2 = new Speaker();
            speaker2.setName("Мария Сидорова");
            speaker2.setEmail("maria.sidorova@example.com");
            speaker2.setBio("Архитектор микросервисов, автор книги 'Микросервисы на практике'");
            speaker2.setCompany("MicroServices Inc.");
            speaker2.setSpecialization("Microservices, Docker, Kubernetes");
            speaker2.setPhoneNumber("+7 (999) 234-56-78");
            speaker2.setSpeakerLevel(Speaker.SpeakerLevel.EXPERT);
            speaker2.setIsFeatured(true);
            speaker2.setAverageRating(4.9);
            speaker2.setTotalRatings(30);
            speaker2.setDeleted(false);

            Speaker speaker3 = new Speaker();
            speaker3.setName("Алексей Смирнов");
            speaker3.setEmail("alexey.smirnov@example.com");
            speaker3.setBio("Cloud Architect, работал в AWS и Google Cloud");
            speaker3.setCompany("CloudExperts");
            speaker3.setSpecialization("AWS, Google Cloud, DevOps");
            speaker3.setPhoneNumber("+7 (999) 345-67-89");
            speaker3.setSpeakerLevel(Speaker.SpeakerLevel.KEYNOTE);
            speaker3.setIsFeatured(true);
            speaker3.setAverageRating(4.7);
            speaker3.setTotalRatings(20);
            speaker3.setDeleted(false);

            speakerRepository.saveAll(List.of(speaker1, speaker2, speaker3));
            log.info("Created {} speakers", speakerRepository.count());

            log.info("Creating participants...");

            // Создание участников с использованием Builder
            Participant participant1 = Participant.builder()
                    .firstName("Анна")
                    .lastName("Иванова")
                    .email("anna.ivanova@example.com")
                    .phone("+7 (911) 111-11-11")
                    .company("Google")
                    .position("Software Engineer")
                    .build();

            Participant participant2 = Participant.builder()
                    .firstName("Дмитрий")
                    .lastName("Козлов")
                    .email("dmitry.kozlov@example.com")
                    .phone("+7 (911) 222-22-22")
                    .company("Yandex")
                    .position("Senior Developer")
                    .build();

            Participant participant3 = Participant.builder()
                    .firstName("Екатерина")
                    .lastName("Морозова")
                    .email("ekaterina.morozova@example.com")
                    .phone("+7 (911) 333-33-33")
                    .company("Microsoft")
                    .position("Team Lead")
                    .build();

            Participant participant4 = Participant.builder()
                    .firstName("Сергей")
                    .lastName("Васильев")
                    .email("sergey.vasiliev@example.com")
                    .phone("+7 (911) 444-44-44")
                    .company("VK")
                    .position("Architect")
                    .build();

            participantRepository.saveAll(List.of(participant1, participant2, participant3, participant4));
            log.info("Created {} participants", participantRepository.count());

            log.info("Creating sessions...");

            // Создание сессий
            Session session1 = new Session();
            session1.setTitle("Введение в Spring Boot 3");
            session1.setDescription("Основные возможности Spring Boot 3 для начинающих");
            session1.setStartTime(LocalDateTime.of(2024, 6, 15, 10, 0));
            session1.setEndTime(LocalDateTime.of(2024, 6, 15, 11, 30));
            session1.setMaxCapacity(100);
            session1.setCurrentParticipants(0);
            session1.setRoomNumber("Зал A");
            session1.setType(Session.SessionType.LECTURE);
            session1.setStatus(Session.SessionStatus.SCHEDULED);
            session1.setRequiresRegistration(true);
            session1.setEvent(javaConference);
            session1.setSpeaker(speaker1);

            Session session2 = new Session();
            session2.setTitle("Микросервисы: от монолита к распределенной системе");
            session2.setDescription("Практический опыт перехода на микросервисы");
            session2.setStartTime(LocalDateTime.of(2024, 6, 15, 14, 0));
            session2.setEndTime(LocalDateTime.of(2024, 6, 15, 15, 30));
            session2.setMaxCapacity(80);
            session2.setCurrentParticipants(0);
            session2.setRoomNumber("Зал B");
            session2.setType(Session.SessionType.WORKSHOP);
            session2.setStatus(Session.SessionStatus.SCHEDULED);
            session2.setRequiresRegistration(true);
            session2.setEvent(javaConference);
            session2.setSpeaker(speaker2);

            Session session3 = new Session();
            session3.setTitle("Cloud Native Applications");
            session3.setDescription("Разработка приложений для облачной среды");
            session3.setStartTime(LocalDateTime.of(2024, 6, 16, 11, 0));
            session3.setEndTime(LocalDateTime.of(2024, 6, 16, 12, 30));
            session3.setMaxCapacity(60);
            session3.setCurrentParticipants(0);
            session3.setRoomNumber("Зал C");
            session3.setType(Session.SessionType.LECTURE);
            session3.setStatus(Session.SessionStatus.SCHEDULED);
            session3.setRequiresRegistration(true);
            session3.setEvent(javaConference);
            session3.setSpeaker(speaker3);

            Session session4 = new Session();
            session4.setTitle("Kubernetes для разработчиков");
            session4.setDescription("Как эффективно использовать Kubernetes в разработке");
            session4.setStartTime(LocalDateTime.of(2024, 6, 16, 14, 30));
            session4.setEndTime(LocalDateTime.of(2024, 6, 16, 16, 0));
            session4.setMaxCapacity(70);
            session4.setCurrentParticipants(0);
            session4.setRoomNumber("Зал A");
            session4.setType(Session.SessionType.WORKSHOP);
            session4.setStatus(Session.SessionStatus.SCHEDULED);
            session4.setRequiresRegistration(true);
            session4.setEvent(javaConference);
            session4.setSpeaker(speaker1);

            Session session5 = new Session();
            session5.setTitle("Основы Spring Security");
            session5.setDescription("Защита приложений с помощью Spring Security");
            session5.setStartTime(LocalDateTime.of(2024, 6, 20, 11, 0));
            session5.setEndTime(LocalDateTime.of(2024, 6, 20, 13, 0));
            session5.setMaxCapacity(40);
            session5.setCurrentParticipants(0);
            session5.setRoomNumber("Zoom Room 1");
            session5.setType(Session.SessionType.LECTURE);
            session5.setStatus(Session.SessionStatus.SCHEDULED);
            session5.setRequiresRegistration(true);
            session5.setEvent(springWorkshop);
            session5.setSpeaker(speaker1);

            sessionRepository.saveAll(List.of(session1, session2, session3, session4, session5));
            log.info("Created {} sessions", sessionRepository.count());

            log.info("Creating registrations...");

            // Создание тестовых регистраций
            Registration registration1 = new Registration();
            registration1.setParticipant(participant1);
            registration1.setEvent(javaConference);
            registration1.setSession(session1);
            registration1.setStatus(Registration.RegistrationStatus.CONFIRMED);
            registration1.setRegistrationDate(LocalDateTime.now());

            Registration registration2 = new Registration();
            registration2.setParticipant(participant2);
            registration2.setEvent(javaConference);
            registration2.setSession(session2);
            registration2.setStatus(Registration.RegistrationStatus.CONFIRMED);
            registration2.setRegistrationDate(LocalDateTime.now());

            Registration registration3 = new Registration();
            registration3.setParticipant(participant3);
            registration3.setEvent(springWorkshop);
            registration3.setSession(session5);
            registration3.setStatus(Registration.RegistrationStatus.CONFIRMED);
            registration3.setRegistrationDate(LocalDateTime.now());

            // Обновляем счетчики участников
            javaConference.incrementParticipants();
            javaConference.incrementParticipants();
            springWorkshop.incrementParticipants();

            session1.incrementParticipants();
            session2.incrementParticipants();
            session5.incrementParticipants();

            // Сохраняем обновленные события и сессии
            eventRepository.saveAll(List.of(javaConference, springWorkshop));
            sessionRepository.saveAll(List.of(session1, session2, session5));

            registrationRepository.saveAll(List.of(registration1, registration2, registration3));
            log.info("Created {} registrations", registrationRepository.count());

            log.info("✅ Data initialization completed successfully!");
            log.info("📊 Summary:");
            log.info("   Events: {}", eventRepository.count());
            log.info("   Speakers: {}", speakerRepository.count());
            log.info("   Participants: {}", participantRepository.count());
            log.info("   Sessions: {}", sessionRepository.count());
            log.info("   Registrations: {}", registrationRepository.count());

        } catch (Exception e) {
            log.error("❌ Error during data initialization: ", e);
            throw new RuntimeException("Data initialization failed", e);
        }
    }
}