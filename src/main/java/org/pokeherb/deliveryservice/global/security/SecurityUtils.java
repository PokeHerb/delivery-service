package org.pokeherb.deliveryservice.global.security;

/**
 * SecurityContextHolder 정보 확인을 위한 util service 제공
 * - 현재 로그인한 사용자 권한 확인
 * - 현재 로그인한 사용자 username 반환
 * */
public interface SecurityUtils {
    // 현재 로그인한 사용자의 권한 확인
    boolean isPermitted(String role);
    // 현재 로그인한 사용자의 username 반환
    String getCurrentUsername();
}
