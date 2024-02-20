package com.hyunrian.project.repository;

import com.hyunrian.project.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
