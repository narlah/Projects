package com.nkosev.urlShortener.NkUrlShortener.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "URLS")
public class URLKeyValPair {

    @Id
    @Column
    public String shortenedUrl;

    @Column
    public String originalUrl;

    public URLKeyValPair(String shortenedUrl, String originalUrl) {
        this.shortenedUrl = shortenedUrl;
        this.originalUrl = originalUrl;
    }

    public URLKeyValPair() {
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
