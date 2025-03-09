package com.urlshortner.urlshortnerapi.controller;

import com.urlshortner.urlshortnerapi.UrlshortnerapiApplication;
import com.urlshortner.urlshortnerapi.model.UrlMapping;
import com.urlshortner.urlshortnerapi.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlshortnerapiApplication.class)
@AutoConfigureMockMvc
public class UrlShortenerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlMappingRepository repository;

    @Test
    public void testShortenUrlEndpoint() throws Exception {
        String url = "https://www.example.com";
        mockMvc.perform(post("/api/shortener")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"" + url + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").exists());
    }

    @Test
    public void testRedirectEndpoint() throws Exception {
        // Create a mapping entry directly in the repository.
        UrlMapping mapping = new UrlMapping("https://www.example.com");
        mapping = repository.save(mapping);
        mapping.setShortCode("abc123");
        repository.save(mapping);

        mockMvc.perform(get("/" + mapping.getShortCode()))
                .andExpect(status().isFound())
                .andExpect(header().exists("Location"));
    }
}
