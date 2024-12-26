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
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean active;

    @Enumerated(EnumType.STRING)
    private StaffRole    role;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}
