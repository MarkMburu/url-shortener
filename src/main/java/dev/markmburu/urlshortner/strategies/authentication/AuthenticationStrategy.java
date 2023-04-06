package dev.markmburu.urlshortner.strategies.authentication;

import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;
import dev.markmburu.urlshortner.exceptios.InvalidOperationException;
import javax.management.InvalidApplicationException;

public interface AuthenticationStrategy {
    AuthenticateResponseDto execute(AuthenticateRequestDto request) throws InvalidApplicationException, InvalidOperationException;
}
