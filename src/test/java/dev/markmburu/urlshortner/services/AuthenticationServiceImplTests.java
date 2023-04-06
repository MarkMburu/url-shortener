package dev.markmburu.urlshortner.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;
import dev.markmburu.urlshortner.exceptios.InvalidOperationException;
import dev.markmburu.urlshortner.factories.interfaces.AuthenticationStrategyFactory;
import dev.markmburu.urlshortner.security.AuthenticatedUserDetails;
import dev.markmburu.urlshortner.security.AuthenticationToken;
import dev.markmburu.urlshortner.strategies.authentication.AuthenticationStrategy;
import java.util.UUID;
import javax.management.InvalidApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service tests")
public class AuthenticationServiceImplTests {

    @Mock
    AuthenticationStrategyFactory authStrategyFactory;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Test
    @DisplayName("Authenticate User Should Successfully call factory")
    public void authenticateUser_ShouldCallFactoryCorrectly() throws InvalidOperationException, InvalidApplicationException {
        // Arrange
        var request = new AuthenticateRequestDto("markm", "#MarkM@123", "password");
        var strategyMock = Mockito.mock(AuthenticationStrategy.class);

        when(authStrategyFactory.create(ArgumentMatchers.anyString()))
                .thenReturn(strategyMock);

        when(strategyMock.execute(ArgumentMatchers.any()))
                .thenReturn(new AuthenticateResponseDto());

        authenticationService.authenticateUser(request);

        verify(authStrategyFactory, Mockito.times(1))
                .create(request.getGrantType());

        verify(strategyMock, Mockito.times(1))
                .execute(ArgumentMatchers.eq(request));
    }

    @Test
    @DisplayName("isAuthenticated returns false if user is not authenticated")
    public void isAuthenticated_shouldReturnTrue_whenUserIsNotAuthenticated() {
        assertFalse(authenticationService.isAuthenticated());
    }

    @Test
    @DisplayName("isAuthenticated Should return true if user is authenticated")
    public void isAuthenticated_ShouldReturnTrueIfUserIsAuthenticated() {
        var ctx = SecurityContextHolder.createEmptyContext();
        var principal = new AuthenticatedUserDetails();
        principal.setId(UUID.randomUUID());
        principal.setName("John Doe");
        principal.setUsername("john.doe");
        principal.setEmail("john@doe.com");
        ctx.setAuthentication(new AuthenticationToken(principal));

        SecurityContextHolder.setContext(ctx);

        assert authenticationService.isAuthenticated();
    }
}
