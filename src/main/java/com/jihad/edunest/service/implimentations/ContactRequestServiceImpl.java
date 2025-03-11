package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.ContactRequest;
import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.ContactStatus;
import com.jihad.edunest.exception.user.UserNotFoundException;
import com.jihad.edunest.repository.ContactRequestRepository;
import com.jihad.edunest.repository.MemberRepository;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.ContactRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactRequestServiceImpl implements ContactRequestService {
    private final ContactRequestRepository contactRequestRepository;
    private final MemberRepository memberRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public ContactRequest createContactRequest(ContactRequestCreateDTO requestDTO, Long memberId, Long schoolId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'id: " + memberId));

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("École non trouvée avec l'id: " + schoolId));

        ContactRequest contactRequest = ContactRequest.builder()
                .subject(requestDTO.getSubject())
                .message(requestDTO.getMessage())
                .contactStatus(ContactStatus.PENDING) // Statut initial
                .createdAt(LocalDateTime.now())
                .member(member)
                .school(school)
                .build();

        return contactRequestRepository.save(contactRequest);
    }

    @Override
    public ContactRequest updateStatus(Long id, ContactStatus newStatus) {
        ContactRequest contactRequest = getContactRequestById(id);
        contactRequest.setContactStatus(newStatus);
        return contactRequestRepository.save(contactRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactRequest getContactRequestById(Long id) {
        return contactRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de contact non trouvée avec l'id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRequest> getContactRequestsByMember(Long memberId) {
        return contactRequestRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRequest> getContactRequestsBySchool(Long schoolId) {
        return contactRequestRepository.findBySchoolId(schoolId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRequest> getContactRequestsByStatus(ContactStatus status) {
        return contactRequestRepository.findByContactStatus(status);
    }

    @Override
    public void deleteContactRequest(Long id) {
        contactRequestRepository.deleteById(id);
    }
}
