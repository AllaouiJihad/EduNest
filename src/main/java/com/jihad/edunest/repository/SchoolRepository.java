package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School,Long> {
    List<School> findByStatus(SchoolStatus status);

}
