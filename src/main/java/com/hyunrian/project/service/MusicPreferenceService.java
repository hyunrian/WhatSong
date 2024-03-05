package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.repository.MusicPreferenceQueryRepository;
import com.hyunrian.project.repository.MusicPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MusicPreferenceService {

    private final MusicPreferenceRepository preferenceRepository;
    private final MusicPreferenceQueryRepository preferenceQueryRepository;
    private final MemberService memberService;

    public MusicPreference savePreference(Member member, String genre, String trackId) {
        return preferenceRepository.save(new MusicPreference(member, genre, trackId));
    }

    public List<MusicPreference> getTopPreference(Member member) {
        List<MusicPreference> preferenceList = preferenceQueryRepository.findByMemberIdInOrder(member.getId());
        return preferenceList;
    }
}
