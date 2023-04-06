package dev.markmburu.urlshortner.factories.interfaces;


import dev.markmburu.urlshortner.strategies.authentication.AuthenticationStrategy;
import dev.markmburu.urlshortner.exceptios.InvalidOperationException;

public interface AuthenticationStrategyFactory {
    /**
     * Creates new AuthenticationStrategy instance.
     * @param method what kind of authentication method should be used.
     * @return the requested authentication strategy
     */
    AuthenticationStrategy create(String method) throws InvalidOperationException;
}
