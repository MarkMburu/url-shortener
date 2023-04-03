package com.markmburu.interview.urlshortener.dto;

public class OriginalUrl {

    private String fullUrl;

    public OriginalUrl() {
    }

    public OriginalUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }


}
