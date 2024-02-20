package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.BrandName;
import com.hyunrian.project.dto.MemberJoinDto;
import com.hyunrian.project.dto.MemberUpdateDto;
import com.hyunrian.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(MemberJoinDto joinDto) {
        Member member = new Member().getJoinMember(joinDto);
        return memberRepository.save(member);
    }

    public void update(Long id, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.updateMember(memberUpdateDto.getNickname(), memberUpdateDto.getPassword(), memberUpdateDto.getImagePath());
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        memberRepository.delete(member);
    }

    public void updateBrand(Long id, BrandName brand) {
        Member member = findById(id).orElseThrow();
        member.updateMusicBrand(brand);
    }

}
