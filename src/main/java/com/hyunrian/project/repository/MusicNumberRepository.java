package com.hyunrian.project.repository;

import com.hyunrian.project.domain.MusicNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicNumberRepository extends JpaRepository<MusicNumber, Long> {
}
