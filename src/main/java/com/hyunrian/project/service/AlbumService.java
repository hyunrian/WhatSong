package com.hyunrian.project.service;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.MusicInAlbum;
import com.hyunrian.project.repository.AlbumRepository;
import com.hyunrian.project.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
        boolean isExisted = musicRepository.existsByTrackId(music.getTrackId());
        Album album = albumRepository.findById(albumId).orElseThrow();
        MusicInAlbum musicInAlbum;

        /**
         * 앨범에 추가할 노래가 테이블에 없으면 저장
         * 영속성 컨텍스트 관리를 위해 경우에 따라 다른 데이터 사용
         */
        if (!isExisted) {
            log.info("Music 테이블에 music 저장 - {}", music);
            Music savedMusic = musicRepository.save(music);
            musicInAlbum = new MusicInAlbum(savedMusic, album);
        } else {
            Music foundMusic = musicRepository.findByTrackId(music.getTrackId()).orElseThrow();
            musicInAlbum = new MusicInAlbum(foundMusic, album);
        }

        album.addMusic(musicInAlbum);
//        musicInAlbumRepository.save(musicInAlbum);
        log.info("album에 music 추가 완료");
    }

    public List<Album> findByMemberId(Long memberId) {
        return albumRepository.findByMemberId(memberId);
    }

    public List<MusicInAlbum> findMusicListByAlbumId(Long albumId) {
        Album album = findById(albumId);
        log.info("album={}", album);
        return album.getMusicInAlbumList();
    }

    public Album findById(Long albumId) {
        return albumRepository.findById(albumId).orElseThrow();
    }

}
