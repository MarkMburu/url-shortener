package dev.markmburu.urlshortner.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import dev.markmburu.urlshortner.dtos.requests.AuthenticateRequestDto;
import dev.markmburu.urlshortner.dtos.responses.AuthenticateResponseDto;
import dev.markmburu.urlshortner.entities.User;
import dev.markmburu.urlshortner.repositories.interfaces.UserRepository;
import dev.markmburu.urlshortner.security.AuthenticatedUserDetails;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserRepository userRepository;

  @Test
  @DirtiesContext
  @DisplayName("should be able to make login correctly")
  public void shouldBeAbleToMakeLoginCorrectly() {
    // Arrange
    final var userDto = User.builder()
        .username("markm")
        .passwordHash(BCrypt.hashpw("#MarkM@123", BCrypt.gensalt()))
        .email("markmburu685@gmail.com")
        .name("MarkMburu")
        .build();

    final var loginDto = AuthenticateRequestDto.builder()
        .username(userDto.getUsername())
        .password("#MarkM@123")
        .grantType("password")
        .build();

    when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(userDto));

    final var response = restTemplate.postForEntity("/api/v1/auth", loginDto, AuthenticateResponseDto.class);

    final var responseBody = Objects.requireNonNull(response.getBody());

    assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
    assertTrue(Objects.nonNull(responseBody.getToken()));
    assertTrue(Objects.nonNull(responseBody.getRefreshToken()));
  }

  @Test
  @DirtiesContext
  @DisplayName("should get current user profile.")
  public void shouldBeAbleToGetCurrentUserProfile() {
    // Arrange
    final var password = "blueScreen#666";
    final var userDto = User.builder()
        .username("markm")
        .passwordHash(BCrypt.hashpw(password, BCrypt.gensalt()))
        .email("markmburu685@gmail.com")
        .name("MarkMburu")
        .build();

    final var loginDto = AuthenticateRequestDto.builder()
        .username(userDto.getUsername())
        .password(password)
        .grantType("password")
        .build();

    when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(userDto));

    final var loginResponse = restTemplate.postForEntity("/api/v1/auth", loginDto, AuthenticateResponseDto.class);
    final var responseBody = Objects.requireNonNull(loginResponse.getBody());

    final var baseUrl = restTemplate.getRootUri() + "/api/v1/auth/profile";
    final var headers = new HttpHeaders();
    headers.set("Authorization", String.format("Bearer %s", responseBody.getToken().getToken()));

    final var response = restTemplate.exchange(
        baseUrl,
        HttpMethod.GET,
        new HttpEntity<AuthenticatedUserDetails>(headers),
        AuthenticatedUserDetails.class);

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
  }

  @Test
  @DirtiesContext
  @DisplayName("should return 401 unauthorized when a profile is not authenticated")
  public void shouldReturn401WhenTryingToGetProfileButNotAuthenticated() {

    final var response = restTemplate.getForEntity("/api/v1/auth/profile", AuthenticatedUserDetails.class);

    assertEquals(HttpStatus.valueOf(401), response.getStatusCode());
  }
}
