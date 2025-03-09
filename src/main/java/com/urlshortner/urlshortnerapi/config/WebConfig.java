package com.urlshortner.urlshortnerapi.config;

import com.urlshortner.urlshortnerapi.filter.RateLimitingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilterRegistration(RateLimitingFilter filter) {
        FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        // Filter applies to all endpoints (/*). You can narrow this down if you like.
        registration.addUrlPatterns("/*");
        // Lower number = higher precedence. Adjust if you have other filters.
        registration.setOrder(1);
        return registration;
    }
}
