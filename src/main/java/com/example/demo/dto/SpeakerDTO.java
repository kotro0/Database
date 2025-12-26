package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SpeakerDTO {
    private Long id;

    @NotBlank(message = "Имя спикера обязательно")
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    private String bio;
    private String company;
    private String specialization;
    private String phoneNumber;
    private String linkedinUrl;
    private String twitterHandle;
    private String websiteUrl;
    private String photoUrl;

    // Используем String вместо SpeakerLevel для простоты
    private String speakerLevel = "REGULAR";

    private Boolean isFeatured = false;
    private Double averageRating = 0.0;
    private Integer totalRatings = 0;
}