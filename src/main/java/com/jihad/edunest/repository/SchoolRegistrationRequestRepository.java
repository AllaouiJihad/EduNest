package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.SchoolRegistrationRequest;
import com.jihad.edunest.domaine.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRegistrationRequestRepository extends JpaRepository<SchoolRegistrationRequest, Long> {
    List<SchoolRegistrationRequest> findByMemberId(Long memberId);
    List<SchoolRegistrationRequest> findByStatus(RequestStatus status);
    List<SchoolRegistrationRequest> findByMemberIdAndStatus(Long memberId, RequestStatus status);
}
