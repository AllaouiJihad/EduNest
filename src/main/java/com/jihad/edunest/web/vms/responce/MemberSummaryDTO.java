package com.jihad.edunest.web.vms.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
