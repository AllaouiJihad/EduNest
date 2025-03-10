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
    Optional<Member> findByUsernameAndDeletedFalse(String username);

    Optional<Member> findByEmailAndDeletedFalse(String email);

    Page<Member> findByUsernameContainingOrEmailContainingAndDeletedFalse(String username,
                                                                            String email,
                                                                            Pageable pageable);

    Optional<Member> findByVerificationToken(String verificationToken);

    List<Member> findAllByVerificationToken(String token);

    Optional<Member> findByPasswordResetToken(String passwordResetToken);

    List<Member> findAllByPasswordResetToken(String token);
}
