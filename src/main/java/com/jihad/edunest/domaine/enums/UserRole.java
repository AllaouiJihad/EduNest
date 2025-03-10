package com.jihad.edunest.domaine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum UserRole {
    MEMBER(
            Set.of(
                    Permission.CAN_VIEW_SCHOOLS,
                    Permission.CAN_COMPARE_SCHOOLS,
                    Permission.CAN_CONTACT_SCHOOLS,
                    Permission.CAN_LEAVE_REVIEWS
            )
    ),

    SCHOOL_STAFF(
            Set.of(
                    Permission.CAN_VIEW_SCHOOLS,
                    Permission.CAN_VIEW_STATISTICS // Statistiques limitées à leur école
            )
    ),

    SCHOOL_ADMIN(
            Stream.of(
                    SCHOOL_STAFF.getPermissions(),
                    Set.of(
                            Permission.CAN_MANAGE_PROFILE,
                            Permission.CAN_PUBLISH_OFFERS
                    )
            ).flatMap(Set::stream).collect(Collectors.toSet())
    ),

    ADMIN(
            Stream.of(
                    MEMBER.getPermissions(),
                    SCHOOL_ADMIN.getPermissions(),
                    Set.of(
                            Permission.CAN_MANAGE_USERS,
                            Permission.CAN_MANAGE_SCHOOLS,
                            Permission.CAN_MODERATE_REVIEWS,
                            Permission.CAN_VIEW_GLOBAL_STATS
                    )
            ).flatMap(Set::stream).collect(Collectors.toSet())
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
