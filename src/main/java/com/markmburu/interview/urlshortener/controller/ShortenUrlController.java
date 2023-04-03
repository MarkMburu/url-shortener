package com.markmburu.interview.urlshortener.controller;

import com.markmburu.interview.urlshortener.dto.ShortenedUrl;
import com.markmburu.interview.urlshortener.utils.UrlUtil;
import com.markmburu.interview.urlshortener.dto.OriginalUrl;
import com.markmburu.interview.urlshortener.error.InvalidError;
import com.markmburu.interview.urlshortener.service.ShortenUrlService;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;

@RestController
public class ShortenUrlController {

    Logger logger = LoggerFactory.getLogger(ShortenUrlController.class);
    protected final ShortenUrlService urlService;
    @Autowired
    public ShortenUrlController(ShortenUrlService shortenUrlService) {
        this.urlService = shortenUrlService;
    }
    @PostMapping("/shorten")
    public ResponseEntity<Object> saveUrl(@RequestBody OriginalUrl originalUrl, HttpServletRequest request) {

        UrlValidator validator = new UrlValidator(
                new String[]{"http", "https"}
        );
        String url = originalUrl.getFullUrl();
        if (!validator.isValid(url)) {
            logger.error("Provide a valid url");

            InvalidError error = new InvalidError("url", originalUrl.getFullUrl(), "Invalid URL");

            return ResponseEntity.badRequest().body(error);
        }
        String baseUrl = null;

        try {
            baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
        } catch (MalformedURLException e) {
            logger.error("Invalid url provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid url", e);
        }

        ShortenedUrl shortenedUrl = urlService.getShortUrl(originalUrl);
        shortenedUrl.setShortUrl(baseUrl + shortenedUrl.getShortUrl());
        return new ResponseEntity<>(shortenedUrl, HttpStatus.OK);
    }
    @GetMapping("/{shortenString}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString) {
        try {
            OriginalUrl originalUrl = urlService.getFullUrl(shortenString);

            logger.info(String.format("Redirecting to "+ originalUrl.getFullUrl()));

            response.sendRedirect(originalUrl.getFullUrl());
        } catch (NoSuchElementException e) {
            logger.error(String.format("No URL found for "+ shortenString +" in the db" ));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            logger.error("Could not redirect to the original url");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to redirect to the original url", e);
        }
    }

}
