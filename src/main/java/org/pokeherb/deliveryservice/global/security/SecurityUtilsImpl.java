package org.pokeherb.deliveryservice.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUtilsImpl implements SecurityUtils {
    @Override
    public boolean isPermitted(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isPermitted = false;
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            isPermitted = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
        }
        return isPermitted;
    }

    @Override
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return "";
    }
}