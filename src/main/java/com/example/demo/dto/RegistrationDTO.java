package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegistrationDTO {
    private Long id;
    private LocalDateTime registrationDate;
    private LocalDateTime confirmationDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;

    // Используем String вместо RegistrationStatus для простоты
    private String status = "PENDING";

    private Integer waitlistPosition;
    private String registrationCode;
    private String notes;
    private String paymentStatus = "PENDING";
    private String paymentId;
    private Double amountPaid = 0.0;

    @NotNull(message = "ID участника обязательно")
    private Long participantId;

    @NotNull(message = "ID сессии обязательно")
    private Long sessionId;
}