package com.urlshortner.urlshortnerapi.controller;

import com.urlshortner.urlshortnerapi.DTO.UrlRequest;
import com.urlshortner.urlshortnerapi.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;
    private final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);


    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/api/shortener")
    public ResponseEntity<Map<String, String>> shortenUrl(@Valid @RequestBody UrlRequest request) {
        String originalUrl = request.getUrl();
        String shortCode = urlShortenerService.shortenUrl(originalUrl);

        Map<String, String> response = new HashMap<>();
        response.put("shortCode", shortCode);

        logger.info("URL shortened: {} -> {}", originalUrl, shortCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", originalUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }
}
