package org.pokeherb.deliveryservice.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 게이트웨이를 통해 넘어온 유저 정보를 SecurityContextHolder에 인증 처리
 * userId, username, roles, email, name 포함
 * */
@Component
public class LoginFilter extends GenericFilterBean {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_ROLES = "X-User-Roles";
    private static final String HEADER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NAME = "X-User-Name";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doLogin((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    private void doLogin(HttpServletRequest request) {
        String userId = request.getHeader(HEADER_USER_ID);
        String userName = request.getHeader(HEADER_USERNAME);
        String roles = request.getHeader(HEADER_ROLES);
        String email = request.getHeader(HEADER_EMAIL);
        String name =  request.getHeader(HEADER_USER_NAME);

        name = name == null ? null : URLDecoder.decode(name, StandardCharsets.UTF_8);

        if (!StringUtils.hasText(userId) || !StringUtils.hasText(userName)) {
            return;
        }

        UserDetails userDetails = CustomUserDetails.builder()
                .userId(UUID.fromString(userId))
                .username(userName)
                .email(email)
                .name(name)
                .roles(roles)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
