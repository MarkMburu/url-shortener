package dev.markmburu.urlshortner.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class JwtProperties {
    @Value("${jwt.secret:localhost}")
    private String secret;

    @Value("${jwt.audience:localhost}")
    private String audience;

    @Value("${jwt.issuer:localhost}")
    private String issuer;

    @Value("${jwt.expiration-seconds:7200}")
    private long expirationTime;

    @Value("${jwt.refresh-expiration-seconds:7200}")
    private long refreshTokenExpirationTime;
}
