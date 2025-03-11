package com.jihad.edunest.web.vms.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolSummaryDTO {
    private Long id;
    private String name;
    private String city;
}
