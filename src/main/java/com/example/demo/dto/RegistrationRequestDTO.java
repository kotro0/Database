package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationRequestDTO {
    @NotNull(message = "ID участника обязательно")
    private Long participantId;

    @NotNull(message = "ID сессии обязательно")
    private Long sessionId;

    private Boolean autoConfirm = true;
}