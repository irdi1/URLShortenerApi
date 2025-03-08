package com.urlshortner.urlshortnerapi.service;

import com.urlshortner.urlshortnerapi.model.UrlMapping;
import com.urlshortner.urlshortnerapi.repository.UrlMappingRepository;
import com.urlshortner.urlshortnerapi.util.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    @Autowired
    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public String shortenUrl(String originalUrl) {
        UrlMapping mapping = new UrlMapping(originalUrl);
        mapping = urlMappingRepository.save(mapping);

        String shortCode = Base62.encode(mapping.getId());
        mapping.setShortCode(shortCode);
        urlMappingRepository.save(mapping);

        return shortCode;
    }

    @Cacheable(value = "urlCache", key = "#shortCode")
    public Optional<String> getOriginalUrl(String shortCode) {
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortCode(shortCode);
        return mapping.map(UrlMapping::getOriginalUrl);
    }

}
