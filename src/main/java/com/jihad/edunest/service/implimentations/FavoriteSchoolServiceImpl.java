package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.FavoriteSchool;
import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.repository.FavoriteSchoolRepository;
import com.jihad.edunest.repository.MemberRepository;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.FavoriteSchoolService;
import com.jihad.edunest.web.vms.responce.school.FavoriteSchoolDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteSchoolServiceImpl implements FavoriteSchoolService {

    private final FavoriteSchoolRepository favoriteSchoolRepository;
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void addFavoriteSchool(String username, Long schoolId, String notes) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("École non trouvée avec l'ID: " + schoolId));

        // Vérifier si l'école est déjà en favoris
        if (favoriteSchoolRepository.existsByMemberIdAndSchoolId(member.getId(), schoolId)) {
            throw new RuntimeException("Cette école est déjà dans vos favoris");
        }

        FavoriteSchool favoriteSchool = new FavoriteSchool();
        favoriteSchool.setMember(member);
        favoriteSchool.setSchool(school);
        favoriteSchool.setAddedDate(LocalDateTime.now());
        favoriteSchool.setNotes(notes);

        favoriteSchoolRepository.save(favoriteSchool);
    }

    @Transactional
    @Override
    public void removeFavoriteSchool(String username, Long schoolId) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        favoriteSchoolRepository.deleteByMemberIdAndSchoolId(member.getId(), schoolId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FavoriteSchoolDTO> getFavoriteSchools(String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        List<FavoriteSchool> favorites = favoriteSchoolRepository.findByMemberIdOrderByAddedDateDesc(member.getId());

        return favorites.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FavoriteSchoolDTO mapToDTO(FavoriteSchool favoriteSchool) {
        School school = favoriteSchool.getSchool();

        return FavoriteSchoolDTO.builder()
                .id(favoriteSchool.getId())
                .schoolId(school.getId())
                .schoolName(school.getName())
                .schoolCity(school.getCity())
                .categoryName(school.getCategory() != null ? school.getCategory().getName() : null)
                .notes(favoriteSchool.getNotes())
                .addedDate(favoriteSchool.getAddedDate())
                .build();
    }
}
