package com.jihad.edunest.domaine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SchoolAdministrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDateTime assignedDate;
    private Boolean isActive;

    // Date de dernière connexion en tant qu'administrateur d'école
    private LocalDateTime lastLogin;

    // Champ optionnel pour des notes administratives
    private String adminNotes;
}
