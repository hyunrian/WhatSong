package com.hyunrian.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.MusicNumber;
import com.hyunrian.project.domain.enums.BrandName;
import com.hyunrian.project.dto.MusicInAlbumDto;
import com.hyunrian.project.dto.MusicAndAlbumInfo;
import com.hyunrian.project.repository.MemberRepository;
import com.hyunrian.project.repository.MusicNumberRepository;
import com.hyunrian.project.repository.MusicQueryRepository;
import com.hyunrian.project.service.AlbumService;
import com.hyunrian.project.service.MusicService;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@SpringBootTest
@Transactional
public class DataTest {

    @Autowired
    MusicService musicService;
    @Autowired
    MusicNumberRepository musicNumberRepository;
    @Autowired
    MusicQueryRepository musicQueryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AlbumService albumService;

    @Test
    void getDataByHttpConnection() throws IOException {
        URL url = new URL("https://api.manana.kr/karaoke.json");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-type", "application/json");
        con.setDoOutput(true);

        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8"));

        while (br.ready()) {
            sb.append(br.readLine());
        }
        System.out.println("br = " + br);
    }

    @Test
    void getDataByRestTemplate() {
        String url = "https://api.manana.kr/karaoke.json";
        RestTemplate template = new RestTemplate();

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List list = template.getForObject(url, List.class);
        System.out.println(list);
    }

    /**
     * API 조회 후 JSON 파싱
     * Music, MusicNumber 테이블에 데이터 저장
     */
    @Test
    @Rollback(value = false)
    void getListByTitle() throws JsonProcessingException {
        String url = "https://api.manana.kr/karaoke/song/";
//        String url = "https://api.manana.kr/karaoke/singer/";
        String title = "휠릴리";
//        String singer = "이수영";
        String brand = "/tj";
        String json = ".json";

        String newUrl = url + title + brand + json;

        RestTemplate template = new RestTemplate();

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

//        List list = template.getForObject(url + title + brand + json, List.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<MyMusic> searchList =
                objectMapper.readValue(template.getForObject(newUrl, String.class),
                        new TypeReference<List<MyMusic>>() {});


//
        Music music = new Music();
        MyMusic data = searchList.get(0);
        music.setTitle(data.getTitle());
        music.setComposer(data.getComposer());
        music.setRelease(data.release);
        music.setLyricist(data.lyricist);
        music.setSinger(data.singer);

        MusicNumber musicNumber = new MusicNumber();
        musicNumber.setMusic(music);
        musicNumber.setNumber(data.getNo());
        musicNumber.setBrandName(BrandName.TJ);
//        musicService.save(music);

        musicNumberRepository.save(musicNumber); // 브랜드도 함께 설정해야 함

//            System.out.println(o);
    }

    /**
     * MusicService Querydsl 조인 조회 테스트
     */
    @Test
    @Rollback(value = false)
    void search() {
//        MemberJoinDto joinDto = new MemberJoinDto();
//        joinDto.setEmail("test@gmail.com");
//        joinDto.setAge(30);
//        joinDto.setGender(Gender.FEMALE);
//        joinDto.setNickname("테스트유저");
//        joinDto.setPassword("123");
//        Member member = new Member().join(joinDto);
//        memberRepository.save(member);
//
//        member.updateMusicBrand(BrandName.TJ);

        Member member = memberRepository.findById(1L).orElse(null);

        Album album = albumService.findById(1L);

        MusicInAlbumDto musicInAlbumDto = new MusicInAlbumDto();
        musicInAlbumDto.setAlbum(album);
        musicInAlbumDto.setBrandName(BrandName.TJ);

        List<MusicAndAlbumInfo> all = musicQueryRepository.findAll(musicInAlbumDto);
        System.out.println("=================");
        for (MusicAndAlbumInfo musicAndAlbumInfo : all) {
            System.out.println(musicAndAlbumInfo);
        }
        System.out.println("=================");
    }


    //String 타입의 JSON 파싱 테스트
    @Test
    void paserByJackson() throws JsonProcessingException {

        String jsonString = "[{\"id\":1,\"name\":\"John\",\"age\":30},{\"id\":2,\"name\":\"Sam\",\"age\":20}]";


        ObjectMapper objectMapper = new ObjectMapper();
        List<MyData> myDataList = objectMapper.readValue(jsonString, new TypeReference<List<MyData>>() {});

        for (MyData myData : myDataList) {
            Long id = myData.id;
            String name = myData.name;
            Integer age = myData.age;
            System.out.println("id = " + id);
            System.out.println("name = " + name);
            System.out.println("age = " + age);
        }

    }

    @Getter
    static class MyData {
        private Long id;
        private String name;
        private Integer age;
        private String address;
    }

    @Data
    static class MyMusic {

        private Long id;
        private String title;
        private String singer;
        private String composer;
        private String lyricist;
        private String release;
        private String no;
        private String brand;

    }
}
