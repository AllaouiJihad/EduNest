package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.ContactRequest;
import com.jihad.edunest.domaine.enums.ContactStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ContactRequestRepository extends JpaRepository<ContactRequest,Long> {
    List<ContactRequest> findByMemberId(Long memberId);
    List<ContactRequest> findBySchoolId(Long schoolId);
    List<ContactRequest> findByContactStatus(ContactStatus status);
    List<ContactRequest> findBySchoolIdAndContactStatus(Long schoolId, ContactStatus status);
    List<ContactRequest> findByMemberIdAndContactStatus(Long memberId, ContactStatus status);
}
