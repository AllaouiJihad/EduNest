package com.jihad.edunest.web.vms.responce.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolComparisonResponse {
    private List<SchoolCompareDTO> schools;
    private Map<String, List<String>> comparisonMatrix; // Format: critère -> liste des valeurs pour chaque école
}
