package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.FavoriteSchool;
import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSchoolRepository extends JpaRepository<FavoriteSchool, Long> {
    List<FavoriteSchool> findByMemberId(Long memberId);

    List<FavoriteSchool> findBySchoolId(Long schoolId);

    Optional<FavoriteSchool> findByMemberAndSchool(Member member, School school);

    boolean existsByMemberAndSchool(Member member, School school);

    void deleteByMemberAndSchool(Member member, School school);
}
