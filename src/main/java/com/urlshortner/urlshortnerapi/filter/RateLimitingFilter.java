package com.urlshortner.urlshortnerapi.filter;


import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    // Rate limit configuration: 100 requests per minute
    private static final long CAPACITY = 100;
    private static final Refill REFILL = Refill.greedy(CAPACITY, Duration.ofMinutes(1));
    private static final Bandwidth LIMIT = Bandwidth.classic(CAPACITY, REFILL);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Use client IP as the key
        String clientIp = httpRequest.getRemoteAddr();

        // Get or create a token bucket for this IP
        Bucket bucket = ipBucketMap.computeIfAbsent(clientIp, ip -> Bucket4j.builder().addLimit(LIMIT).build());


        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            httpResponse.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            chain.doFilter(request, response);
        } else {
            // No tokens left - respond with 429 Too Many Requests
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too Many Requests - Rate limit exceeded");
        }
    }
}
