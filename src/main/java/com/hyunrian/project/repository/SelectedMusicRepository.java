package com.hyunrian.project.repository;

import com.hyunrian.project.domain.MusicInAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedMusicRepository extends JpaRepository<MusicInAlbum, Long> {
}
