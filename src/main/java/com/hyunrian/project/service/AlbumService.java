package com.hyunrian.project.service;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.MusicInAlbum;
import com.hyunrian.project.repository.AlbumRepository;
import com.hyunrian.project.repository.MusicRepository;
import com.hyunrian.project.repository.SelectedMusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final MusicRepository musicRepository;
    private final MemberService memberService;

    @Transactional
    public Album createAlbum(String albumName, String nickname) {
        Member member = memberService.findByNickname(nickname).orElseThrow();
        Album album = new Album(albumName, member);
        return albumRepository.save(album);
    }

    @Transactional
    public void addMusicToAlbum(Long albumId, Music music) {
        Music foundMusic = musicRepository.findById(music.getId()).orElse(null);
        if (foundMusic == null) { //앨범에 추가할 노래가 테이블에 없으면 저장
            musicRepository.save(music);
        }

        Album album = albumRepository.findById(albumId).orElseThrow();
        album.addMusicToAlbum(music);
    }

    public List<Album> findByMemberId(Long memberId) {
        return albumRepository.findByMemberId(memberId);
    }

    public List<Music> findMusicListByAlbumId(Long albumId) {
        Album album = findById(albumId);
        return album.getMusicList();
    }

    public Album findById(Long albumId) {
        return albumRepository.findById(albumId).orElseThrow();
    }

}
