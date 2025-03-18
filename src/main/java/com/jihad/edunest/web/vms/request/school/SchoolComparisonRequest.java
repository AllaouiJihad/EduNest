package com.jihad.edunest.web.vms.request.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolComparisonRequest {
    private List<Long> schoolIds;
}
