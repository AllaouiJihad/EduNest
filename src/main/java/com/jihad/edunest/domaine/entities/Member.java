package com.jihad.edunest.domaine.entities;

import com.jihad.edunest.domaine.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected Boolean active;
    private String email;
    private String password;
    private String phone;

    private Boolean deleted = false;
    private String verificationToken;
    private Boolean verified = false;
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToOne(fetch = FetchType.LAZY)
    private School school;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContactRequest> contactRequests;


    @Enumerated(EnumType.STRING)
    private UserRole role;



    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteSchool> favoriteSchools;

    // Méthodes utiles
    public void addFavoriteSchool(School school) {
        if (this.favoriteSchools == null) {
            this.favoriteSchools = new ArrayList<>();
        }
        this.favoriteSchools.add(new FavoriteSchool(this, school));
    }

    public void removeFavoriteSchool(School school) {
        if (this.favoriteSchools != null) {
            this.favoriteSchools.removeIf(fs -> fs.getSchool().getId().equals(school.getId()));
        }
    }






    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role != null ? this.role.getAuthorities() : List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active != null && this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active != null && this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active != null && this.active;
    }
}
