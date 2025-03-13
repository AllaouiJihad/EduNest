package com.jihad.edunest.service;

import com.jihad.edunest.web.vms.request.school.SchoolRegistrationRequestDto;
import com.jihad.edunest.web.vms.request.school.SchoolRegistrationReviewDto;
import com.jihad.edunest.web.vms.responce.school.SchoolRegistrationResponseDto;

import java.util.List;

public interface SchoolRegistrationService {
    SchoolRegistrationResponseDto submitSchoolRegistrationRequest(SchoolRegistrationRequestDto requestDto);
    List<SchoolRegistrationResponseDto> getMemberRequests();
    List<SchoolRegistrationResponseDto> getPendingRequests();
    SchoolRegistrationResponseDto reviewRegistrationRequest(SchoolRegistrationReviewDto reviewDto);


}
