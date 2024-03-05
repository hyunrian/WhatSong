package com.hyunrian.project.repository;

import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.domain.QMusicPreference;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hyunrian.project.domain.QMusicPreference.*;

@Repository
public class MusicPreferenceQueryRepository {

    private final JPAQueryFactory query;
    private final MusicPreferenceRepository preferenceRepository;

    public MusicPreferenceQueryRepository(EntityManager em, MusicPreferenceRepository preferenceRepository) {
        this.query = new JPAQueryFactory(em);
        this.preferenceRepository = preferenceRepository;
    }

    public List<MusicPreference> findByMemberIdInOrder(Long memberId) {

        return query
                .select(musicPreference)
                .from(musicPreference)
                .where(musicPreference.member.id.eq(memberId))
                .groupBy(musicPreference.genre, musicPreference.id)
                .orderBy(musicPreference.genre.count().desc())
                .fetch();
    }

//    private BooleanExpression likeSinger(String singer) {
//        if (StringUtils.hasText(singer)) {
//            return music.singer.like("%" + singer + "%");
//        }
//        return null;
//    }
//
//    private BooleanExpression likeTitle(String title) {
//        if (StringUtils.hasText(title)) {
//            return music.singer.like("%" + title + "%");
//        }
//        return null;
//    }
}
