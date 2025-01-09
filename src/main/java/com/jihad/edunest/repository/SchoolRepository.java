package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SchoolRepository extends JpaRepository<School,Long> {
}
