package com.markmburu.interview.urlshortener.dto;

public class ShortenedUrl {

    private String shortUrl;

    public ShortenedUrl() {
    }

    public ShortenedUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

}
