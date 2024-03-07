package com.hyunrian.project.repository;

import com.hyunrian.project.domain.MusicPreference;
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
                .limit(5)
                .fetch();
    }
}
