package dev.markmburu.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class UrlShortenerApplication{
	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}
}
