package com.jihad.edunest.web.vms.mapper;

import com.jihad.edunest.domaine.entities.ContactRequest;
import com.jihad.edunest.web.vms.responce.ContactRequestDTO;
import com.jihad.edunest.web.vms.responce.MemberSummaryDTO;
import com.jihad.edunest.web.vms.responce.SchoolSummaryDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContactRequestMapper {
    public static ContactRequestDTO toDTO(ContactRequest contactRequest) {
        if (contactRequest == null) {
            return null;
        }

        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setId(contactRequest.getId());
        dto.setSubject(contactRequest.getSubject());
        dto.setMessage(contactRequest.getMessage());
        dto.setContactStatus(contactRequest.getContactStatus());
        dto.setCreatedAt(contactRequest.getCreatedAt());

        if (contactRequest.getMember() != null) {
            MemberSummaryDTO memberDTO = new MemberSummaryDTO();
            memberDTO.setId(contactRequest.getMember().getId());
            memberDTO.setFirstName(contactRequest.getMember().getFirstName());
            memberDTO.setLastName(contactRequest.getMember().getLastName());
            memberDTO.setEmail(contactRequest.getMember().getEmail());
            dto.setMember(memberDTO);
        }

        if (contactRequest.getSchool() != null) {
            SchoolSummaryDTO schoolDTO = new SchoolSummaryDTO();
            schoolDTO.setId(contactRequest.getSchool().getId());
            schoolDTO.setName(contactRequest.getSchool().getName());
            schoolDTO.setCity(contactRequest.getSchool().getCity());
            dto.setSchool(schoolDTO);
        }

        return dto;
    }

    public static List<ContactRequestDTO> toDTOList(List<ContactRequest> contactRequests) {
        if (contactRequests == null) {
            return Collections.emptyList();
        }

        return contactRequests.stream()
                .map(ContactRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
}
