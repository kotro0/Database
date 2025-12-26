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
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String company;

    private String position;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<Registration> registrations = new ArrayList<>();

    // Метод для получения полного имени
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Builder method
    public static ParticipantBuilder builder() {
        return new ParticipantBuilder();
    }

    public static class ParticipantBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String company;
        private String position;

        public ParticipantBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ParticipantBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ParticipantBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ParticipantBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public ParticipantBuilder company(String company) {
            this.company = company;
            return this;
        }

        public ParticipantBuilder position(String position) {
            this.position = position;
            return this;
        }

        public Participant build() {
            Participant participant = new Participant();
            participant.setFirstName(firstName);
            participant.setLastName(lastName);
            participant.setEmail(email);
            participant.setPhone(phone);
            participant.setCompany(company);
            participant.setPosition(position);
            participant.setRegistrationDate(LocalDateTime.now());
            participant.setIsActive(true);
            return participant;
        }
    }
}