package com.hyunrian.project.repository;

import com.hyunrian.project.domain.MusicPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicPreferenceRepository extends JpaRepository<MusicPreference, Long> {


}
