package dev.markmburu.urlshortner.services.interfaces;

import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;
import dev.markmburu.urlshortner.exceptios.InvalidOperationException;
import dev.markmburu.urlshortner.security.AuthenticatedUserDetails;
import javax.management.InvalidApplicationException;

public interface AuthenticationService {
    /**
     * Checks whether user is authenticated or not.
     */
    boolean isAuthenticated();

    /**
     * Gets authenticated user details from SecurityContext.
     */
    AuthenticatedUserDetails getAuthenticatedUser();

    /**
     * Tries to authenticate user with username/password.
     * @param request Authenticate request containing user credentials.
     * @return AuthenticatedResponseDto containing user details and jwt token.
     * @throws InvalidApplicationException throws if jwt creation fails.
     */
    AuthenticateResponseDto authenticateUser(AuthenticateRequestDto request) throws InvalidApplicationException, InvalidOperationException;
}
