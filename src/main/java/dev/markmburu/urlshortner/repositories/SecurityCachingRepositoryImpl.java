package dev.markmburu.urlshortner.repositories;

import dev.markmburu.urlshortner.repositories.interfaces.SecurityCachingRepository;
import dev.markmburu.urlshortner.dtos.SecurityTokenTicket;
import dev.markmburu.urlshortner.dtos.responses.JwtToken;
import dev.markmburu.urlshortner.entities.User;

import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SecurityCachingRepositoryImpl extends BaseCachingRepository<UUID, SecurityTokenTicket> implements SecurityCachingRepository {
    public SecurityCachingRepositoryImpl(RedisTemplate<UUID, SecurityTokenTicket> cache) {
        super(cache);
    }


    @Override
    public void storeRefreshToken(JwtToken token, User user) {

    }
}
