package com.hyunrian.project.repository;

import com.hyunrian.project.domain.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByToken(String token);
}
