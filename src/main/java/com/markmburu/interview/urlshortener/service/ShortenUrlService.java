package com.markmburu.interview.urlshortener.service;

import com.markmburu.interview.urlshortener.dto.OriginalUrl;
import com.markmburu.interview.urlshortener.utils.ConvertUtils;
import com.markmburu.interview.urlshortener.dto.ShortenedUrl;
import com.markmburu.interview.urlshortener.model.ShortenUrlEntity;
import com.markmburu.interview.urlshortener.repository.ShortenUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortenUrlService {

    Logger logger = LoggerFactory.getLogger(ShortenUrlService.class);

    private final ShortenUrlRepository shortenUrlRepository;

    @Autowired
    public ShortenUrlService(ShortenUrlRepository shortenUrlRepository) {
        this.shortenUrlRepository = shortenUrlRepository;
    }

    private ShortenUrlEntity get(Long id) {
        logger.info(String.format("Fetching Url from database for Id %d", id));
        ShortenUrlEntity shortenUrlEntity = shortenUrlRepository.findById(id).get();
        return shortenUrlEntity;
    }

    public OriginalUrl getFullUrl(String shortenString) {
        logger.debug("Converting Base 62 string %s to Base 10 id");
        Long id = ConvertUtils.strToId(shortenString);
        logger.info(String.format("Converted Base 62 string %s to Base 10 id %s", shortenString, id));

        logger.info(String.format("Retrieving full url for %d", id));
        return new OriginalUrl(this.get(id).getFullUrl());
    }

    private ShortenUrlEntity save(OriginalUrl originalUrl) {
        return shortenUrlRepository.save(new ShortenUrlEntity(originalUrl.getFullUrl()));
    }
    public ShortenedUrl getShortUrl(OriginalUrl originalUrl) {

        logger.info("Checking if the url already exists");
        List<ShortenUrlEntity> savedUrls = null;
        savedUrls = checkFullUrlAlreadyExists(originalUrl);

        ShortenUrlEntity savedUrl = null;

        if (savedUrls.isEmpty()) {
            logger.info(String.format("Saving Url %s to database", originalUrl.getFullUrl()));
            savedUrl = this.save(originalUrl);
            logger.debug(savedUrl.toString());
        }
        else {
            savedUrl = savedUrls.get(0);
            logger.info(String.format("url: %s already exists in the database. skipped insert", savedUrl));
        }

        logger.debug(String.format("Converting Base 10 %d to Base 62 string", savedUrl.getId()));
        String shortUrlText = ConvertUtils.idToStr(savedUrl.getId());
        logger.info(String.format("Converted Base 10 %d to Base 62 string %s", savedUrl.getId(), shortUrlText));

        return new ShortenedUrl(shortUrlText);
    }

    private List<ShortenUrlEntity> checkFullUrlAlreadyExists(OriginalUrl originalUrl) {
        return shortenUrlRepository.findUrlByFullUrl(originalUrl.getFullUrl());
    }
}
