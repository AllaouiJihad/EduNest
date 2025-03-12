package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByVerificationToken(String token);
    Optional<Member> findByPasswordResetToken(String token);
    boolean existsByEmail(String email);
}
