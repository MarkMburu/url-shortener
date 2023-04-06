package dev.markmburu.urlshortner.services.interfaces;

import dev.markmburu.urlshortner.security.AuthenticatedUserDetails;

/**
 * Custom implementation of UserDetailsService
 */
public interface CustomUserDetailsService {
    AuthenticatedUserDetails getUserByUsername(String username);
}
