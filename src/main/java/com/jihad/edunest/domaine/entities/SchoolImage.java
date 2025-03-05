package com.jihad.edunest.domaine.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String caption;
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;
}
