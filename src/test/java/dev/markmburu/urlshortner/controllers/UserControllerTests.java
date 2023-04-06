package dev.markmburu.urlshortner.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dev.markmburu.urlshortner.dtos.ApiError;
import dev.markmburu.urlshortner.dtos.UserDto;
import dev.markmburu.urlshortner.entities.User;
import dev.markmburu.urlshortner.repositories.interfaces.UserRepository;
import java.util.HashMap;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;


@AutoConfigureDataMongo
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
	@Autowired
	private TestRestTemplate restTemplate;

    @MockBean
    private UserRepository userRepository;

	@Test
	@DisplayName("should create a user")
	public void shouldCreateUserCorrectly() {
		// Arrange
		final var userDto = UserDto.builder()
				.name("MarkMburu")
				.email("markmburu685@gmail.com")
				.password("#MarkM@123")
				.username("markm")
				.build();

        when(userRepository.save(any())).thenAnswer((Answer<User>) (invocation) -> invocation.getArgument(0));

		final var response = restTemplate.postForEntity("/api/v1/user", userDto, UserDto.class);

		assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
	}

	@Test
	@DisplayName("should return 400 when name is not present.")
	public void shouldReturn400WithoutName() {
		var userDto = UserDto.builder()
			.email("email@test.com")
			.password("pass")
			.username("username")
			.build();

		// used only for getting the class type on runtime.
		final var testObject = new ApiError<HashMap<String, String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		final var response = restTemplate.postForEntity("/api/v1/user", userDto, testObject.getClass());

		final var responseBody = Objects.requireNonNull(response.getBody());
		final var errors = (HashMap<String, String>) responseBody.getErrors();

		assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
		assertEquals("Validation failed", responseBody.getMessage());
		assertEquals("must not be empty", errors.get("name"));
	}

	@Test
	@DisplayName("should return 400 when email is not present.")
	public void shouldReturn400_whenEmailIsMissing() {
		var userDto = UserDto.builder()
				.name("name")
				.password("pass")
				.username("username")
				.build();

		final var testObject = new ApiError<HashMap<String, String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		final var response = restTemplate.postForEntity("/api/v1/user", userDto, testObject.getClass());

		final var responseBody = Objects.requireNonNull(response.getBody());
		final var errors = (HashMap<String, String>) responseBody.getErrors();

		assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
		assertEquals("Validation failed", responseBody.getMessage());
		assertEquals("Email address is required.", errors.get("email"));
	}

	@Test
	@DisplayName("should return 400 when password is not present.")
	public void shouldReturn400_whenPasswordIsInvalid() {
		var userDto = UserDto.builder()
				.name("name")
				.password("1234")
				.email("email@test.com")
				.username("username")
				.build();

		final var testObject = new ApiError<HashMap<String, String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		final var response = restTemplate.postForEntity("/api/v1/user", userDto, testObject.getClass());

		final var responseBody = Objects.requireNonNull(response.getBody());
		final var errors = (HashMap<String, String>) responseBody.getErrors();

		assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
		assertEquals("Validation failed", responseBody.getMessage());
		assertEquals("Password should be 5 or more chars.", errors.get("password"));
	}
}
