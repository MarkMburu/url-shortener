package dev.markmburu.urlshortner.factories;

import dev.markmburu.urlshortner.strategies.authentication.AuthenticationStrategy;
import dev.markmburu.urlshortner.exceptios.InvalidOperationException;
import dev.markmburu.urlshortner.factories.interfaces.AuthenticationStrategyFactory;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationStrategyFactoryImpl implements AuthenticationStrategyFactory {
    private final Map<String, AuthenticationStrategy> _availableStrategies;
    @Override
    public AuthenticationStrategy create(String method) throws InvalidOperationException {
        var strategyName = getAuthenticationStrategyName(method);
        return Optional
            .of(_availableStrategies.get(strategyName))
            .orElseThrow(InvalidOperationException::new);
    }

    private String getAuthenticationStrategyName(String grantType) throws InvalidOperationException {
        return switch (grantType.toLowerCase()) {
            case "password" -> "PasswordAuthenticationStrategy";
            case "refresh-token"  -> "RefreshTokenAuthenticationStrategy";
            default -> throw new InvalidOperationException();
        };
    }
}
