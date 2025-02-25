package com.jihad.edunest.service.implimentations;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.SchoolService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;

    // Créer une école
    @Transactional
    public School createSchool(School school) {
        school.setStatus(SchoolStatus.PENDING); // Par défaut, l'école est active
        return schoolRepository.save(school);
    }

    // Récupérer une école par son ID
    public Optional<School> getSchoolById(Long id) {
        return schoolRepository.findById(id);
    }

    // Récupérer toutes les écoles
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    // Mettre à jour une école
    @Transactional
    public School updateSchool(Long id, School updatedSchool) {
        return schoolRepository.findById(id)
                .map(school -> {
                    school.setName(updatedSchool.getName());
                    school.setAddress(updatedSchool.getAddress());
                    school.setPostalCode(updatedSchool.getPostalCode());
                    school.setCity(updatedSchool.getCity());
                    school.setPhoneNumber(updatedSchool.getPhoneNumber());
                    school.setEmail(updatedSchool.getEmail());
                    school.setWebsite(updatedSchool.getWebsite());
                    school.setDescription(updatedSchool.getDescription());
                    school.setStatus(updatedSchool.getStatus());
                    return schoolRepository.save(school);
                })
                .orElseThrow(() -> new RuntimeException("École non trouvée avec l'ID : " + id));
    }

    // Supprimer une école
    @Transactional
    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }

    // Rechercher des écoles par statut
    public List<School> getSchoolsByStatus(SchoolStatus status) {
        return schoolRepository.findByStatus(status);
    }
}
