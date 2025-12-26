package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ParticipantDTO {
    private Long id;

    @NotBlank(message = "Полное имя обязательно")
    private String fullName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    private String phoneNumber;
    private String organization;
    private String position;
    private LocalDateTime dateOfBirth;

    // Используем String вместо RegistrationType для простоты
    private String registrationType = "REGULAR";

    private String dietaryRestrictions;
    private String specialNeeds;
    private Boolean isVerified = false;
    private Boolean isSubscribed = true;
    private Integer totalRegistrations = 0;
    private Integer attendedSessions = 0;
}