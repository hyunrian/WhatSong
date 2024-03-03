package com.hyunrian.project.repository;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByMemberId(Long memberId);
}
