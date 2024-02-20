package com.hyunrian.project.repository;

import com.hyunrian.project.domain.*;
import com.hyunrian.project.domain.enums.BrandName;
import com.hyunrian.project.dto.MusicInAlbumDto;
import com.hyunrian.project.dto.MusicAndAlbumInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.hyunrian.project.domain.QMusic.*;
import static com.hyunrian.project.domain.QMusicNumber.*;
import static com.hyunrian.project.domain.QSelectedMusic.*;

@Repository
public class MusicQueryRepository {

    private final JPAQueryFactory query;
    private final MemberRepository memberRepository;

    public MusicQueryRepository(EntityManager em, MemberRepository memberRepository) {
        this.query = new JPAQueryFactory(em);
        this.memberRepository = memberRepository;
    }

    public List<MusicAndAlbumInfo> findAll(MusicInAlbumDto musicInAlbumDto) {

        BrandName brandName = musicInAlbumDto.getBrandName();
        Album album = musicInAlbumDto.getAlbum();

        return query
                .select(Projections.bean(MusicAndAlbumInfo.class,
                        QAlbum.album.albumName,
                        music.singer,
                        music.title,
                        musicNumber.number,
                        music.composer,
                        music.lyricist,
                        music.release,
                        music.likeCount))
                .from(music)
                .join(musicNumber).on(music.eq(musicNumber.music))
                .join(selectedMusic).on(music.eq(selectedMusic.music))
//                .where(likeSinger(singer), likeTitle(title), musicNumber.brandName.eq(brandName), music.eq(musicNumber.music))
                .where(musicNumber.brandName.eq(brandName),
                        music.eq(musicNumber.music),
                        selectedMusic.album.eq(album))
                .fetch();
    }

    private BooleanExpression likeSinger(String singer) {
        if (StringUtils.hasText(singer)) {
            return music.singer.like("%" + singer + "%");
        }
        return null;
    }

    private BooleanExpression likeTitle(String title) {
        if (StringUtils.hasText(title)) {
            return music.singer.like("%" + title + "%");
        }
        return null;
    }
}
