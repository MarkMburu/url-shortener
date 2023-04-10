package dev.markmburu.urlshortner.controllers;

import dev.markmburu.urlshortner.dtos.ShortenedUrlDto;
import dev.markmburu.urlshortner.dtos.requests.CreateShortUrlRequest;
import dev.markmburu.urlshortner.services.interfaces.ShortenerService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/shortener")
@PreAuthorize("permitAll()")
public class ShortenerController {
    private final ShortenerService shortenerService;

    public ShortenerController(ShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ShortenedUrlDto> shortUrl(@Valid @RequestBody CreateShortUrlRequest request)
            throws
            AccessDeniedException,
            URISyntaxException {

        return ResponseEntity
                .created(new URI(""))
                .body(shortenerService.createShortUrl(request));
    }

    @GetMapping("/{shortVersion}")
    public ResponseEntity<ShortenedUrlDto> findUrl(@PathVariable String shortVersion) {
        var result = shortenerService.findShortUrlBy(shortVersion);
        return ResponseEntity.ok().body(result);
    }
}
