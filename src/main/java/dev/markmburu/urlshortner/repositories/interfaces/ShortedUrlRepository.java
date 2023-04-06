package dev.markmburu.urlshortner.repositories.interfaces;

import dev.markmburu.urlshortner.entities.ShortenedUrl;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShortedUrlRepository extends PagingAndSortingRepository<ShortenedUrl, String>, CrudRepository<ShortenedUrl, String> {
    boolean existsByShortenedVersion(String shortenedVersion);
    Optional<ShortenedUrl> findByShortenedVersion(String shortenedVersion);
}
