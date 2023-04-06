package dev.markmburu.urlshortner.strategies.authentication;

import dev.markmburu.urlshortner.repositories.interfaces.SecurityCachingRepository;
import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;

import javax.management.InvalidApplicationException;
import org.springframework.stereotype.Component;

@Component("RefreshTokenAuthenticationStrategy")
public class RefreshTokenAuthenticationStrategy implements AuthenticationStrategy {
    private final SecurityCachingRepository _cacheRepository;

    public RefreshTokenAuthenticationStrategy(SecurityCachingRepository cacheRepository) {
        _cacheRepository = cacheRepository;
    }

    @Override
    public AuthenticateResponseDto execute(AuthenticateRequestDto request) throws InvalidApplicationException {
        return null;
    }
}
