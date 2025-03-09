package com.urlshortner.urlshortnerapi.service;

import com.urlshortner.urlshortnerapi.exception.ShortCodeNotFoundException;
import com.urlshortner.urlshortnerapi.model.UrlMapping;
import com.urlshortner.urlshortnerapi.repository.UrlMappingRepository;
import com.urlshortner.urlshortnerapi.util.Base62;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


public class UrlShortenerServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlShortenerService service;

    public UrlShortenerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl() {
        String originalUrl = "https://www.example.com";

        // Mock repository.save(...) to simulate the DB assigning an ID
        when(repository.save(any(UrlMapping.class))).thenAnswer(invocation -> {
            UrlMapping um = invocation.getArgument(0);
            um.setId(1L); // Simulate generated ID from the DB
            return um;
        });

        String shortCode = service.shortenUrl(originalUrl);
        assertNotNull(shortCode);
        // If your Base62 encoding of 1 is "1" or something else, assert accordingly
        assertEquals(Base62.encode(1L), shortCode);
    }

    @Test
    public void testGetOriginalUrlNotFound() {
        assertThrows(ShortCodeNotFoundException.class, () -> {
            service.getOriginalUrl("nonExistentCode");
        });
    }
}
