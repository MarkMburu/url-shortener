package dev.markmburu.urlshortner.services;

import dev.markmburu.urlshortner.services.interfaces.AuthenticationService;
import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;
import dev.markmburu.urlshortner.exceptios.InvalidOperationException;
import dev.markmburu.urlshortner.factories.interfaces.AuthenticationStrategyFactory;
import dev.markmburu.urlshortner.security.AuthenticatedUserDetails;
import dev.markmburu.urlshortner.security.AuthenticationToken;

import java.util.Objects;
import javax.management.InvalidApplicationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationStrategyFactory _authStrategyFactory;

    public AuthenticationServiceImpl(AuthenticationStrategyFactory authStrategyFactory) {
        _authStrategyFactory = authStrategyFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticateResponseDto authenticateUser(AuthenticateRequestDto request)
            throws
            InvalidOperationException,
            InvalidApplicationException {
        var strategy = _authStrategyFactory.create(request.getGrantType());
        return strategy.execute(request);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAuthenticated() {
        final var ctx = SecurityContextHolder.getContext();
        final var authentication = ctx.getAuthentication();
        if (Objects.isNull(authentication)) {
            return false;
        }

        final var isCorrectToken = authentication.getClass().isAssignableFrom(AuthenticationToken.class);
        return isCorrectToken && ctx
                .getAuthentication()
                .isAuthenticated();
    }

    /**
     * {@inheritDoc}
     */
    public AuthenticatedUserDetails getAuthenticatedUser() throws AccessDeniedException {
        var ctx = SecurityContextHolder.getContext();
        if (!(ctx.getAuthentication() instanceof AuthenticationToken)) {
            throw new AccessDeniedException("User not authenticated");
        }

        return (AuthenticatedUserDetails) ctx.getAuthentication().getPrincipal();
    }
}
