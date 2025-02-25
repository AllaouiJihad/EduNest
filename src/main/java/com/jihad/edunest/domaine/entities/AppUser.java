package com.jihad.edunest.domaine.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public abstract  class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected Boolean active;

}
