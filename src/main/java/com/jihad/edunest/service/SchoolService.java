package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.web.vms.request.school.SchoolSearchRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolDetailsResponse;
import com.jihad.edunest.web.vms.responce.school.SchoolSearchResponse;
import org.springframework.data.domain.Page;

public interface SchoolService {
    Page<SchoolSearchResponse> searchSchools(SchoolSearchRequest request);
    SchoolDetailsResponse getSchoolDetails(Long schoolId);

}
