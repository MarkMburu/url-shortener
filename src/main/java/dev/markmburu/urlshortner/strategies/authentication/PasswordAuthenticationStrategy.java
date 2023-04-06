package dev.markmburu.urlshortner.strategies.authentication;

import dev.markmburu.urlshortner.repositories.interfaces.SecurityCachingRepository;
import dev.markmburu.urlshortner.repositories.interfaces.UserRepository;
import dev.markmburu.urlshortner.dtos.UserDto;
import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;
import dev.markmburu.urlshortner.services.interfaces.SecurityService;
import javax.management.InvalidApplicationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("PasswordAuthenticationStrategy")
public class PasswordAuthenticationStrategy implements AuthenticationStrategy {
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final SecurityCachingRepository _securityCache;

    public PasswordAuthenticationStrategy(
            UserRepository userRepository,
            SecurityService securityService,
            SecurityCachingRepository securityCache) {
        this.userRepository = userRepository;
        this.securityService = securityService;
        _securityCache = securityCache;
    }

    @Override
    public AuthenticateResponseDto execute(AuthenticateRequestDto request) throws InvalidApplicationException {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->  new AccessDeniedException("Invalid username or password"));

        var isPasswordValid = securityService.verifyPassword(request.getPassword(), user.getPasswordHash());
        if (!isPasswordValid) {
            throw new AccessDeniedException("Invalid username or password");
        }

        var generatedJwt = securityService.generateToken(user);
        var refreshToken = securityService.generateRefreshToken(user);

        _securityCache.storeRefreshToken(refreshToken, user);

        return new AuthenticateResponseDto(
                new UserDto(user),
                generatedJwt,
                refreshToken);
    }
}
