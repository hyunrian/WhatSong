package com.hyunrian.project.repository;

import com.hyunrian.project.domain.SelectedMusic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedMusicRepository extends JpaRepository<SelectedMusic, Long> {
}
