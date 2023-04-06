package dev.markmburu.urlshortner.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.javafaker.Faker;
import dev.markmburu.urlshortner.dtos.requests.CreateShortUrlRequest;
import dev.markmburu.urlshortner.entities.ShortenedUrl;
import dev.markmburu.urlshortner.exceptios.EntityNotFoundException;
import dev.markmburu.urlshortner.repositories.interfaces.CachingShortedUrlRepository;
import dev.markmburu.urlshortner.repositories.interfaces.ShortedUrlRepository;
import dev.markmburu.urlshortner.security.AuthenticatedUserDetails;
import dev.markmburu.urlshortner.services.interfaces.AuthenticationService;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;

@DisplayName("Shortener Service tests")
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
public class ShortenerServiceImplTests {

    @Mock
    ShortedUrlRepository shortedUrlRepository;

    @Mock
    AuthenticationService authenticationService;

    @Mock
    CachingShortedUrlRepository cachingShortedUrlRepository;

    @InjectMocks
    ShortenerServiceImpl shortenerService;

    @Test
    @DisplayName("Should throw exception if max retries")
    public void shouldThrowExceptionIfRetryLimitReached() throws AccessDeniedException {
        // Arrange
        var request = new CreateShortUrlRequest();

        Mockito.when(shortedUrlRepository.existsByShortenedVersion(ArgumentMatchers.any()))
                .thenReturn(true,true, true);

        // Act
        assertThrows(RuntimeException.class, () -> shortenerService.createShortUrl(request));
    }

    @Test
    @DisplayName("Should create url as public if user is not logged-in")
    public void shouldCreatePublicShortenedUrl() throws AccessDeniedException {
        // Arrange

        var request = new CreateShortUrlRequest("https://github.com/MarkMburu");

        Mockito.when(authenticationService.isAuthenticated()).thenReturn(false);

        Mockito.when(shortedUrlRepository.save(ArgumentMatchers.any(ShortenedUrl.class)))
                .thenAnswer((Answer<ShortenedUrl>) invocationOnMock -> invocationOnMock.getArgument(0));

        var result = shortenerService.createShortUrl(request);

        assert Objects.equals(result.getOriginalUrl(), request.getUrl());

        Mockito.verify(shortedUrlRepository, Mockito.times(1))
                .save(ArgumentMatchers.argThat(ShortenedUrl::isPublic));

    }

    @Test
    @DisplayName("Should create url as not public if user is logged-in")
    public void shouldCreatePrivateShortenedUrl() throws AccessDeniedException {
        var request = new CreateShortUrlRequest("https://github.com/MarkMburu");
        var faker = new Faker();
        var userId = UUID.randomUUID();
        var authenticatedUser = new AuthenticatedUserDetails(
                faker.internet().emailAddress(),
                faker.name().fullName(),
                "some.username",
                userId);

        Mockito.when(authenticationService.isAuthenticated()).thenReturn(true);

        Mockito.when(authenticationService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        Mockito.when(shortedUrlRepository.save(ArgumentMatchers.any(ShortenedUrl.class)))
                .thenAnswer((Answer<ShortenedUrl>) invocationOnMock -> invocationOnMock.getArgument(0));

        var result = shortenerService.createShortUrl(request);

        assert Objects.equals(result.getOriginalUrl(), request.getUrl());

        Mockito.verify(shortedUrlRepository, Mockito.times(1))
                .save(ArgumentMatchers.argThat(shortenedUrl -> {
                    return !shortenedUrl.isPublic() && shortenedUrl.getUserId().equals(userId);
                }));
    }

    @Test
    @DisplayName("Should return from cache")
    public void shouldReturnFromCacheCorrectly() {
        var randomId = "abCd";
        var expectedObj = new ShortenedUrl("https://google.com", randomId, UUID.randomUUID(), false);

        Mockito.when(cachingShortedUrlRepository.findByShortVersion(ArgumentMatchers.eq(randomId)))
                .thenReturn(Optional.of(expectedObj));

        var result = shortenerService.findShortUrlBy(randomId);

        Mockito.verify(shortedUrlRepository, Mockito.times(0))
                .findByShortenedVersion(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should return from cache")
    public void shouldThrowIfShortenedUrlNotFound() {
        var randomId = "abCd";

        Mockito.when(cachingShortedUrlRepository.findByShortVersion(ArgumentMatchers.eq(randomId)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shortenerService.findShortUrlBy(randomId));
    }

    @Test
    @DisplayName("Should go to database if not in cache.")
    public void shouldCallDatabaseIfCacheNotPresent() {
        var randomId = "abCd";
        var expectedObj = new ShortenedUrl("https://google.com", randomId, UUID.randomUUID(), false);

        Mockito.when(shortedUrlRepository.findByShortenedVersion(ArgumentMatchers.eq(randomId)))
                .thenReturn(Optional.of(expectedObj));

        var result = shortenerService.findShortUrlBy(randomId);

        Mockito.verify(shortedUrlRepository, Mockito.times(1))
                .findByShortenedVersion(ArgumentMatchers.any());

        Mockito.verify(cachingShortedUrlRepository, Mockito.times(1))
                .findByShortVersion(ArgumentMatchers.any());
    }
}
