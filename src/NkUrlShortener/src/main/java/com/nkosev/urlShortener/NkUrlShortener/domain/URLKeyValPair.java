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
    public String urlShortened;

    @Column
    public String urlOriginal;

    public URLKeyValPair(String urlShortened, String urlOriginal) {
        this.urlShortened = urlShortened;
        this.urlOriginal = urlOriginal;
    }

    public URLKeyValPair() {
    }

    public String getUrlShortened() {
        return urlShortened;
    }

    public void setUrlShortened(String urlShortened) {
        this.urlShortened = urlShortened;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }
}
