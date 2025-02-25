package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;

import java.util.List;
import java.util.Optional;

public interface SchoolService {
    School createSchool(School school);
    Optional<School> getSchoolById(Long id);
    List<School> getAllSchools();
    School updateSchool(Long id, School updatedSchool);
    void deleteSchool(Long id);
    List<School> getSchoolsByStatus(SchoolStatus status);
    }
