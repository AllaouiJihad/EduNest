package com.jihad.edunest.domaine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UserRole {
    PARENT(
            Set.of(
                    Permission.CAN_VIEW_SCHOOLS,
                    Permission.CAN_COMPARE_SCHOOLS,
                    Permission.CAN_CONTACT_SCHOOLS
            )
    ),

    SCHOOL_ADMIN(
            Set.of(
                    Permission.CAN_MANAGE_PROFILE,
                    Permission.CAN_PUBLISH_OFFERS,
                    Permission.CAN_VIEW_STATISTICS
            )
    ),

    PLATFORM_ADMIN(
            Set.of(
                    Permission.CAN_MANAGE_USERS,
                    Permission.CAN_MANAGE_SCHOOLS,
                    Permission.CAN_MODERATE_REVIEWS,
                    Permission.CAN_VIEW_GLOBAL_STATS
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}
