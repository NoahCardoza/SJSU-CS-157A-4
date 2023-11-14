package com.example.demo;

public class Env {
    static public String get(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = System.getProperty(key);
            if (value == null) {
                throw new RuntimeException("Environment variable " + key + " not found");
            }
        }
        return value;
    }

    static public String get(String key, String defaultValue) {
        try {
            return get(key);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }
}
