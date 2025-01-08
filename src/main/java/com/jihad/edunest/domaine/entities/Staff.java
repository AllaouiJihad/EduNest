package com.jihad.edunest.domaine.entities;

import com.jihad.edunest.domaine.enums.StaffRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff extends AppUser{


    @Enumerated(EnumType.STRING)
    private StaffRole  role;
    private String subject; //Matière enseignée
    private String gradeLevel; // Niveau scolaire (exemple : Primaire, Collège, Lycée)
    private int experienceYears;
    private String educationLevel; //Niveau d'études ou spécialisation (exemple : Master en Mathématiques).

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

}
