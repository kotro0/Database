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
@Table(name = "speakers")
public class Speaker {

    public enum SpeakerLevel {
        REGULAR, EXPERIENCED, EXPERT, KEYNOTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 1000)
    private String bio;

    private String company;
    private String specialization;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "twitter_handle")
    private String twitterHandle;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "photo_url")
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "speaker_level")
    private SpeakerLevel speakerLevel = SpeakerLevel.REGULAR;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    // ИСПРАВЛЕНО: убрать precision и scale для Double
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Связи
    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    // Бизнес-методы
    public void addRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5");
        }

        double newTotal = (this.averageRating * this.totalRatings) + rating;
        this.totalRatings++;
        this.averageRating = newTotal / this.totalRatings;
    }
    // Builder для DataInitializer
    public static SpeakerBuilder builder() {
        return new SpeakerBuilder();
    }

    public static class SpeakerBuilder {
        private String name;
        private String email;
        private String bio;
        private String company;
        private String specialization;
        private String phoneNumber;
        private String linkedinUrl;
        private String twitterHandle;
        private String websiteUrl;
        private String photoUrl;
        private SpeakerLevel speakerLevel = SpeakerLevel.REGULAR;
        private Boolean isFeatured = false;
        private Double averageRating = 0.0;
        private Integer totalRatings = 0;

        public SpeakerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SpeakerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public SpeakerBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public SpeakerBuilder company(String company) {
            this.company = company;
            return this;
        }

        public SpeakerBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public SpeakerBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public SpeakerBuilder linkedinUrl(String linkedinUrl) {
            this.linkedinUrl = linkedinUrl;
            return this;
        }

        public SpeakerBuilder twitterHandle(String twitterHandle) {
            this.twitterHandle = twitterHandle;
            return this;
        }

        public SpeakerBuilder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public SpeakerBuilder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public SpeakerBuilder speakerLevel(SpeakerLevel speakerLevel) {
            this.speakerLevel = speakerLevel;
            return this;
        }

        public SpeakerBuilder isFeatured(Boolean isFeatured) {
            this.isFeatured = isFeatured;
            return this;
        }

        public SpeakerBuilder averageRating(Double averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        public SpeakerBuilder totalRatings(Integer totalRatings) {
            this.totalRatings = totalRatings;
            return this;
        }

        public Speaker build() {
            Speaker speaker = new Speaker();
            speaker.setName(name);
            speaker.setEmail(email);
            speaker.setBio(bio);
            speaker.setCompany(company);
            speaker.setSpecialization(specialization);
            speaker.setPhoneNumber(phoneNumber);
            speaker.setLinkedinUrl(linkedinUrl);
            speaker.setTwitterHandle(twitterHandle);
            speaker.setWebsiteUrl(websiteUrl);
            speaker.setPhotoUrl(photoUrl);
            speaker.setSpeakerLevel(speakerLevel);
            speaker.setIsFeatured(isFeatured);
            speaker.setAverageRating(averageRating);
            speaker.setTotalRatings(totalRatings);
            speaker.setDeleted(false);
            return speaker;
        }
    }
}