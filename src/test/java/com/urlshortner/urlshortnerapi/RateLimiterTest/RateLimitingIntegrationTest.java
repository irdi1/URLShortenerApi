package com.urlshortner.urlshortnerapi.RateLimiterTest;

import com.urlshortner.urlshortnerapi.UrlshortnerapiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UrlshortnerapiApplication.class)
@AutoConfigureMockMvc
public class RateLimitingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnderLimit() throws Exception {
        // We only make 1 request, expecting 200
        mockMvc.perform(post("/api/shortener")
                        .with(request -> { request.setRemoteAddr("127.0.0.1"); return request; })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://www.example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRateLimitExceeded() throws Exception {
        // Use a different IP address for this scenario
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(post("/api/shortener")
                    .with(request -> { request.setRemoteAddr("127.0.0.2"); return request; })
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"url\":\"https://www.example.com/test" + i + "\"}"));
        }
        // The 6th request should fail with 429
        mockMvc.perform(post("/api/shortener")
                        .with(request -> { request.setRemoteAddr("127.0.0.2"); return request; })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://www.example.com/test-final\"}"))
                .andExpect(status().isTooManyRequests());
    }
}
