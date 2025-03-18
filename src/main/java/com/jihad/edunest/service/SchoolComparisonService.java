package com.jihad.edunest.service;

import com.jihad.edunest.web.vms.request.school.SchoolComparisonRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolComparisonResponse;

public interface SchoolComparisonService {
    SchoolComparisonResponse compareSchools(SchoolComparisonRequest request);
}
