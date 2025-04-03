package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.web.vms.request.school.SchoolSearchRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolDetailsResponse;
import com.jihad.edunest.web.vms.responce.school.SchoolSearchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SchoolService {
    Page<SchoolSearchResponse> searchSchools(SchoolSearchRequest request);
    SchoolDetailsResponse getSchoolDetails(Long schoolId);


    Page<SchoolSearchResponse> getAllSchools(int page, int size, String sortBy, String sortDirection);

    /**
     * Get all active schools (no pagination)
     */
    List<SchoolSearchResponse> getAllActiveSchools();


    /**
     * Delete a school
     */
    boolean deleteSchool(Long schoolId);

    /**
     * Get schools by category
     */
    Page<SchoolSearchResponse> getSchoolsByCategory(Long categoryId, int page, int size);

    /**
     * Get schools by city
     */
    Page<SchoolSearchResponse> getSchoolsByCity(String city, int page, int size);
}
