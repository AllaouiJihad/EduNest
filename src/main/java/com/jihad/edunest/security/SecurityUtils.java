package com.jihad.edunest.security;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.UserRole;

public class SecurityUtils {

    /**
     * Vérifie si un membre peut gérer une école spécifique
     * @param member Le membre qui tente d'accéder
     * @param school L'école concernée
     * @return true si l'accès est autorisé, false sinon
     */
    public static boolean canManageSchool(Member member, School school) {
        // Vérifie si le membre est un admin de plateforme
        if (member.getRole() == UserRole.ADMIN) {
            return true;
        }

        // Vérifie si le membre est l'admin de cette école spécifique
        if (member.getRole() == UserRole.SCHOOL_ADMIN) {
            // Recherche si ce membre est associé à cette école
            return isSchoolAdministrator(member, school);
        }

        return false;
    }

    /**
     * Vérifie si un membre est administrateur d'une école spécifique
     * @param member Le membre
     * @param school L'école
     * @return true si le membre est admin de cette école, false sinon
     */
    public static boolean isSchoolAdministrator(Member member, School school) {
        // Utilise la nouvelle relation via l'objet administrator
        return school.getAdministrator() != null &&
                school.getAdministrator().getMember() != null &&
                school.getAdministrator().getMember().getId().equals(member.getId());
    }

    /**
     * Vérifie si un membre fait partie du personnel d'une école
     * @param member Le membre
     * @param school L'école
     * @return true si le membre fait partie du staff de cette école, false sinon
     */
    public static boolean isSchoolStaff(Member member, School school) {
        if (school.getStaff() == null) return false;

        return school.getStaff().stream()
                .anyMatch(staff -> staff.getMember() != null &&
                        staff.getMember().getId().equals(member.getId()));
    }

    /**
     * Vérifie si un membre peut voir les statistiques d'une école
     * @param member Le membre
     * @param school L'école
     * @return true si le membre peut voir les statistiques, false sinon
     */
    public static boolean canViewSchoolStatistics(Member member, School school) {
        // Admin plateforme peut voir toutes les statistiques
        if (member.getRole() == UserRole.ADMIN) {
            return true;
        }

        // Admin d'école ou staff peuvent voir les stats de leur école
        if (member.getRole() == UserRole.SCHOOL_ADMIN || member.getRole() == UserRole.SCHOOL_STAFF) {
            return isSchoolAdministrator(member, school) || isSchoolStaff(member, school);
        }

        return false;
    }
}
