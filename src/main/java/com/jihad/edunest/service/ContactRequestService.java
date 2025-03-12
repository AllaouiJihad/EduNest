package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.ContactRequest;
import com.jihad.edunest.domaine.enums.ContactStatus;
import com.jihad.edunest.web.vms.request.ContactRequestCreateDTO;

import java.util.List;

public interface ContactRequestService {
    ContactRequest createContactRequest(ContactRequestCreateDTO requestDTO, Long memberId, Long schoolId);
    ContactRequest updateStatus(Long id, ContactStatus newStatus);
    ContactRequest getContactRequestById(Long id);
    List<ContactRequest> getContactRequestsByMember(Long memberId);
    List<ContactRequest> getContactRequestsBySchool(Long schoolId);
    List<ContactRequest> getContactRequestsByStatus(ContactStatus status);
    void deleteContactRequest(Long id);
}
