package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.SchoolImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SchoolImageRepository extends JpaRepository<SchoolImage,Long> {
    List<SchoolImage> findBySchoolIdOrderBySortOrder(Long schoolId);
    Optional<SchoolImage> findFirstBySchoolIdOrderBySortOrder(Long schoolId);
}
