package com.jihad.edunest.domaine.entities;

import com.jihad.edunest.domaine.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends AppUser{
    private String email;
    private String password;
    private String phone;
    private UserRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToOne(fetch = FetchType.LAZY)
    private School school;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContactRequest> contactRequests;

}
