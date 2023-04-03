package com.markmburu.interview.urlshortener;

import com.markmburu.interview.urlshortener.service.ShortenUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private ShortenUrlService shortenUrlService;

	@Test
	void contextLoads() {
	}

}
