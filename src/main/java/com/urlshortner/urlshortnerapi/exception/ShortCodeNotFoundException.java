package com.urlshortner.urlshortnerapi.exception;

public class ShortCodeNotFoundException extends RuntimeException {

    public ShortCodeNotFoundException(String shortCode) {
        super("Short Code not found: " + shortCode);
    }
}
