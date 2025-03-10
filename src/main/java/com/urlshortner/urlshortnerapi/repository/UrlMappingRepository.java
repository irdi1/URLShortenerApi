package com.urlshortner.urlshortnerapi.repository;

import com.urlshortner.urlshortnerapi.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortcode);
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
}
