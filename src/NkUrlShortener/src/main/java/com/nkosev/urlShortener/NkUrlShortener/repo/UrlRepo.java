package com.nkosev.urlShortener.NkUrlShortener.repo;

import com.nkosev.urlShortener.NkUrlShortener.domain.URLKeyValPair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlRepo extends CrudRepository<URLKeyValPair, String> {
    List<URLKeyValPair> findByOriginalUrl(String originalUrl);
}
