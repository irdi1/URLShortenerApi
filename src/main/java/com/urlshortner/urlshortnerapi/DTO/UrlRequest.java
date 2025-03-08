package com.urlshortner.urlshortnerapi.DTO;


public class UrlRequest {
    @jakarta.validation.constraints.NotBlank(message = "URL is required")
    @org.hibernate.validator.constraints.URL(message = "Invalid URL format")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
