package dev.markmburu.urlshortner.repositories.interfaces;

import dev.markmburu.urlshortner.entities.User;
import java.util.Optional;

public interface CachingUserRepository {
    Optional<User> findById(String id);
}
