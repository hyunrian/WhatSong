package com.hyunrian.project.service;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.SelectedMusic;
import com.hyunrian.project.repository.AlbumRepository;
import com.hyunrian.project.repository.MusicRepository;
import com.hyunrian.project.repository.SelectedMusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final MusicRepository musicRepository;
    private final SelectedMusicRepository selectedMusicRepository;

    @Transactional
    public void addMusicToAlbum(Long albumId, Music music) {
        Music foundMusic = musicRepository.findById(music.getId()).orElse(null);
        Album album = albumRepository.findById(albumId).orElseThrow();
        if (foundMusic == null) { //앨범에 추가할 노래가 테이블에 없으면 저장
            musicRepository.save(music);
        }
        SelectedMusic selectedMusic = new SelectedMusic(foundMusic, album);
        selectedMusicRepository.save(selectedMusic);
    }

    public Album findById(Long albumId) {
        return albumRepository.findById(albumId).orElseThrow();
    }

}
