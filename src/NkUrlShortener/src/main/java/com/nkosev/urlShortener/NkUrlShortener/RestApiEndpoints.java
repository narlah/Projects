package com.nkosev.urlShortener.NkUrlShortener;

import com.nkosev.urlShortener.NkUrlShortener.domain.URLKeyValPair;
import com.nkosev.urlShortener.NkUrlShortener.repo.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/smallUrl")
public class RestApiEndpoints {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    @Value("${author.name}")
    String authorName;

    @Autowired
    UrlRepo urlRepo;

    public RestApiEndpoints() throws NoSuchAlgorithmException {
    }

    @RequestMapping(method = RequestMethod.GET)
    public URLShortenedResponse postURLForShortening(@RequestParam(name = "url") String shortUrl) {
        URLShortenedResponse urlShortenedResponse = new URLShortenedResponse();
        urlShortenedResponse.setShortenedUrl(shortUrl);
        if (urlRepo.existsById(shortUrl)) {
            Optional urlOptional = urlRepo.findById(shortUrl);
            urlShortenedResponse.setOriginalUrl(((URLKeyValPair) urlOptional.get()).urlOriginal);
        } else {
            urlShortenedResponse.setOriginalUrl("Nope , not in the database , you poor soul!");
        }
        return urlShortenedResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    public URLShortenedResponse postURLForShortening(@RequestBody URLShortThisRequest shortThisRequest) {
        URLShortenedResponse urlShort = new URLShortenedResponse();
        urlShort.setOriginalUrl(shortThisRequest.getRequestThis());
        urlShort.setShortenedUrl(shortenUrl(shortThisRequest.getRequestThis()));
        List<URLKeyValPair> list = urlRepo.findByUrlOriginal(shortThisRequest.getRequestThis());
        if (list.size() == 0)
            urlRepo.save(new URLKeyValPair(urlShort.shortenedUrl, urlShort.originalUrl));
        else
            urlShort.setShortenedUrl(list.get(0).urlShortened);
        return urlShort;
    }

    @RequestMapping(value = "/author", method = RequestMethod.GET)
    public String getAuthor(){
        return authorName;
    }

    private String shortenUrl(@RequestBody String shortThisRequest) {
        //TODO for now md5, will change shorting method later
        return digest.digest(shortThisRequest.getBytes()).toString();
    }
}
