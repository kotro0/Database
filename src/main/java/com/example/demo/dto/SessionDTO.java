package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionDTO {
    private Long id;

    @NotBlank(message = "Название сессии обязательно")
    private String title;

    private String description;

    @NotNull(message = "Время начала обязательно")
    private LocalDateTime startTime;

    @NotNull(message = "Время окончания обязательно")
    private LocalDateTime endTime;

    @NotNull(message = "Максимальная вместимость обязательна")
    @Min(value = 1, message = "Максимальная вместимость должна быть не менее 1")
    private Integer maxCapacity = 50;

    private Integer currentParticipants = 0;
    private String roomNumber;

    // Используем String вместо enum для простоты
    private String type = "LECTURE";
    private String status = "SCHEDULED";

    private Boolean requiresRegistration = true;

    @NotNull(message = "ID события обязательно")
    private Long eventId;

    private Long speakerId;
}