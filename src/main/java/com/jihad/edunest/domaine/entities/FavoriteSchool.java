package com.jihad.edunest.domaine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteSchool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDateTime addedDate;

    private String notes; // Notes personnelles de l'utilisateur sur cette école

    // Constructeur qui initialise automatiquement la date
    public FavoriteSchool(Member member, School school) {
        this.member = member;
        this.school = school;
        this.addedDate = LocalDateTime.now();
    }
}
