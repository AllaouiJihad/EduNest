package com.jihad.edunest.domaine.entities;

import com.jihad.edunest.domaine.enums.StaffRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Staff{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject; //Matière enseignée
    private String gradeLevel; // Niveau scolaire (exemple : Primaire, Collège, Lycée)
    private int experienceYears;
    private String educationLevel; //Niveau d'études ou spécialisation (exemple : Master en Mathématiques).

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Member Member;



}
