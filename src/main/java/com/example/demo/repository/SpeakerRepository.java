package com.example.demo.repository;

import com.example.demo.model.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

    Optional<Speaker> findByEmail(String email);

    List<Speaker> findByCompany(String company);

    List<Speaker> findByIsFeaturedTrue();

    List<Speaker> findBySpeakerLevel(Speaker.SpeakerLevel level);
}