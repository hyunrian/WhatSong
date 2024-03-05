package com.hyunrian.project.repository;

import com.hyunrian.project.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {

    Optional<Music> findByTrackId(String trackId);

    boolean existsByTrackId(String trackId);
}
