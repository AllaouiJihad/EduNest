package com.jihad.edunest.domaine.entities;

import com.jihad.edunest.domaine.enums.SchoolStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;
    private String postalCode;
    private String city;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private SchoolStatus status;
    /*private Float rating;*/

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
    @OneToOne(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SchoolAdministrator administrator;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Staff> staff;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContactRequest> contactRequests;


    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteSchool> favoritedBy;

    // Méthode utile
    public int getFavoriteCount() {
        return favoritedBy != null ? favoritedBy.size() : 0;
    }




}
