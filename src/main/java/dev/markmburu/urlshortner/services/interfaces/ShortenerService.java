package dev.markmburu.urlshortner.services.interfaces;

import dev.markmburu.urlshortner.dtos.ShortenedUrlDto;
import dev.markmburu.urlshortner.dtos.requests.CreateShortUrlRequest;

public interface ShortenerService {
    ShortenedUrlDto createShortUrl(CreateShortUrlRequest request);
    ShortenedUrlDto findShortUrlBy(String urlId);
}
