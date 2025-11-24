package org.pokeherb.deliveryservice.global.security;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class CustomUserDetails implements UserDetails {

    private final UUID userId;
    private final String username;
    private final String name;
    private final String email;
    private final String roles;

    @Builder
    public CustomUserDetails(UUID userId, String username, String name, String email, String roles) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> userRoles = StringUtils.hasText(roles) ? Arrays.stream(roles.split(",")).toList() : List.of("ROLE_USER");
        return userRoles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }
}