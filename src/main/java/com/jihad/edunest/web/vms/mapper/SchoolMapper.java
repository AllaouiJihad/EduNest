package com.jihad.edunest.web.vms.mapper;

import com.jihad.edunest.domaine.entities.Review;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.web.vms.request.CategoryDTO;
import com.jihad.edunest.web.vms.responce.SchoolResponseDTO;

public class SchoolMapper {
    public static SchoolResponseDTO toResponseDTO(School school) {
        SchoolResponseDTO dto = new SchoolResponseDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setAddress(school.getAddress());
        dto.setPostalCode(school.getPostalCode());
        dto.setCity(school.getCity());
        dto.setPhoneNumber(school.getPhoneNumber());
        dto.setEmail(school.getEmail());
        dto.setWebsite(school.getWebsite());
        dto.setDescription(school.getDescription());
        dto.setStatus(school.getStatus());

        if (school.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(school.getCategory().getId());
            categoryDTO.setName(school.getCategory().getName());
            categoryDTO.setType(school.getCategory().getType());
            dto.setCategory(categoryDTO);
        }

        // Calculer la note moyenne à partir des revues
        if (school.getReviews() != null && !school.getReviews().isEmpty()) {
            double avgRating = school.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
        }

        dto.setFavoriteCount(school.getFavoriteCount());

        return dto;
    }
}
