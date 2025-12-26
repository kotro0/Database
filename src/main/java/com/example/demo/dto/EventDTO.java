package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDTO {
    private Long id;

    @NotBlank(message = "Название события обязательно")
    private String name;

    private String description;

    @NotNull(message = "Дата начала обязательна")
    private LocalDateTime startDate;

    @NotNull(message = "Дата окончания обязательна")
    private LocalDateTime endDate;

    @NotBlank(message = "Место проведения обязательно")
    private String location;

    @Min(value = 1, message = "Максимальное количество участников должно быть не менее 1")
    private Integer maxParticipants;

    private Integer currentParticipants = 0;

    // Используем String вместо EventStatus для простоты
    private String status = "PLANNED";

    private String organizerEmail;
    private String organizerPhone;
    private LocalDateTime registrationDeadline;
    private Double price = 0.0;
    private String currency = "RUB";
    private Boolean isPublic = true;
    private Boolean requiresApproval = false;
}