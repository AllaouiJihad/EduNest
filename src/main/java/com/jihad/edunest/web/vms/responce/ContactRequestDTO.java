package com.jihad.edunest.web.vms.responce;

import com.jihad.edunest.domaine.enums.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDTO {
    private Long id;
    private String subject;
    private String message;
    private ContactStatus contactStatus;
    private LocalDateTime createdAt;
    private MemberSummaryDTO member;
    private SchoolSummaryDTO school;
}
