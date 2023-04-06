package dev.markmburu.urlshortner;

import dev.markmburu.urlshortner.repositories.interfaces.SecurityCachingRepository;
import dev.markmburu.urlshortner.repositories.interfaces.ShortedUrlRepository;
import dev.markmburu.urlshortner.repositories.interfaces.UserRepository;
import dev.markmburu.urlshortner.repositories.interfaces.CachingShortedUrlRepository;
import dev.markmburu.urlshortner.repositories.interfaces.CachingUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = UrlShortenerApplication.class)
@ActiveProfiles("test")
public class UrlShortenerTests {

	@MockBean
	private CachingShortedUrlRepository repository;

	@MockBean
	private SecurityCachingRepository securityCachingRepository;

	@MockBean
	private CachingUserRepository cachingUserRepository;

	@MockBean
	private ShortedUrlRepository shortedUrlRepository;

	@MockBean
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}
}

