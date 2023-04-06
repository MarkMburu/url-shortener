package dev.markmburu.urlshortner.repositories.interfaces;

import dev.markmburu.urlshortner.entities.ShortenedUrl;
import java.util.Optional;

public interface CachingShortedUrlRepository {
    Optional<ShortenedUrl> findById(String id);
    Optional<ShortenedUrl> findByShortVersion(String id);
    boolean saveByShortVersion(ShortenedUrl shortenedUrl);
}
