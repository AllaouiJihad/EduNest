package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.SchoolAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolAdministratorRepository extends JpaRepository<SchoolAdministrator,Long> {
Optional<SchoolAdministrator> findBySchoolIdAndMemberId(Long schoolId, Long memberId);
    Optional<SchoolAdministrator> findByMemberId(Long memberId);
    Optional<SchoolAdministrator> findBySchoolId(Long schoolId);
    boolean existsByMemberId(Long memberId);
}
