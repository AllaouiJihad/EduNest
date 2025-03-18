package com.jihad.edunest.web.vms.responce.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolImageDTO {
    private Long id;
    private String url;
    private String caption;
}
