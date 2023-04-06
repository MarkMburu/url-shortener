package dev.markmburu.urlshortner.repositories.interfaces;

import dev.markmburu.urlshortner.dtos.responses.JwtToken;
import dev.markmburu.urlshortner.entities.User;

public interface SecurityCachingRepository {
    void storeRefreshToken(JwtToken token, User user);
}
