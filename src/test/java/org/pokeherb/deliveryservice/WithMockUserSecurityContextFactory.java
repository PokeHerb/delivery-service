package org.pokeherb.deliveryservice;

import org.pokeherb.deliveryservice.global.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.UUID;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<MockUser> {
    @Override
    public SecurityContext createSecurityContext(MockUser user) {

        UserDetails userDetails = CustomUserDetails.builder()
                .userId(UUID.fromString(user.uuid()))
                .username(user.username())
                .name(user.name())
                .email(user.email())
                .roles(String.join(",", Arrays.stream(user.roles()).map(s -> "ROLE_" + s).toList()))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication); // 로그인 처리

        return context;
    }
}

