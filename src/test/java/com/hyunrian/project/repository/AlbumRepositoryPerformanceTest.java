package com.hyunrian.project.repository;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.MusicInAlbum;
import com.hyunrian.project.dto.member.MemberJoinDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:tcp://localhost/~/projectTest",
        "spring.datasource.username=sa",
        "spring.jpa.hibernate.ddl-auto:update"
})
public class AlbumRepositoryPerformanceTest {

    @Autowired AlbumRepository albumRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MusicRepository musicRepository;

    @BeforeEach
    void setData() {
//        saveMember();
//        saveData();
    }

//    @Test
//    void testReadOnlyLoadingTime() {
//        Member member = saveMember();
//        saveData(member);
//        long trueTime = readOnlyTrue(member.getId());
//        long falseTime = readOnlyFalse(member.getId());
//
//    }



    @Test
    @Transactional
    void readOnlyFalse() {
//        Member member = saveMember();
//        saveData(member);

        long startTime = System.currentTimeMillis();

        List<Album> albumList = albumRepository.findByMemberId(1L);
        Long id = albumList.get(0).getId();
        albumRepository.findById(id);

        long endTime = System.currentTimeMillis();

        long falseTime = endTime - startTime;
        log.info("falseTime={}", falseTime); //148 145

    }

    @Test
    @Transactional(readOnly = true)
    void readOnlyTrue() {
//        Member member = saveMember();
//        saveData(member);

        long startTime = System.currentTimeMillis();

        List<Album> albumList = albumRepository.findByMemberId(1L);
        Long id = albumList.get(0).getId();
        albumRepository.findById(id);

        long endTime = System.currentTimeMillis();

        long trueTime = endTime - startTime;
        log.info("trueTime={}", trueTime); //147 151

    }

    @Test
    void saveMember() {
        MemberJoinDto dto = new MemberJoinDto();
        dto.setNickname("tester");
        dto.setPassword("123123");
        dto.setEmail("tester@mail.com");
        Member member = new Member().getJoinMember(dto);
        memberRepository.save(member);
    }

    @Test
    void createAlbum() {
        Member member = memberRepository.findByNickname("tester").orElse(null);
        Album album = new Album("Tester's Album2", member);
        albumRepository.save(album);
        log.info("앨범 생성 완료");
    }

    @Test
    void saveMusic() {
        for (int i = 0; i < 200; i++) {
            Music music = new Music("trackId" + i, "trackName" + i,
                    "artistId" + i, "artistName" + i, "imageUrl");
            musicRepository.save(music);
            log.info("음악 생성 완료");
        }
    }

    @Test
    void saveMusicToAlbum() {
        List<Music> musicList = musicRepository.findAll();
        List<Album> list = albumRepository.findAll();
        Album album = albumRepository.findById(list.get(2).getId()).orElseThrow();
        for (Music music : musicList) {
            album.addMusic(new MusicInAlbum(music, album));
        }
    }

}
