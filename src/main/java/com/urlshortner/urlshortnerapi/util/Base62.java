package com.urlshortner.urlshortnerapi.util;

public class Base62 {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int remainder = (int) (num % BASE);
            sb.append(ALPHABET.charAt(remainder));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
